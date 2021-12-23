package io.suite.venus.job.admin.core.util;



import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IdWorker {

	/*
	 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
	 * <P>1、第一位为未使用
	 * <P>2、接下来的41位为毫秒级时间(41位的长度可以使用69年)
	 * <P>3、然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点） 
	 * <P>最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
	 */
	private static long workerId;
	@Value("${workerId:1}")
    public void setWorkerId(String value) {
		workerId = Long.parseLong(value);  
    }  
	private static long datacenterId;
	@Value("${centerId:1}")
    public void setDatacenterId(String value) {
		datacenterId = Long.parseLong(value);  
    }  
	private long sequence = 0L;
	private long twepoch = 1288834974657L;
	private long workerIdBits = 5L;//5位长度的工作机器id
	private long datacenterIdBits = 5L;//5位长度的数据中心id
	private long maxWorkerId = -1L ^ (-1L << workerIdBits);
	private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	private long sequenceBits = 12L;//12位长度序列号
	private long workerIdShift = sequenceBits;
	private long datacenterIdShift = sequenceBits + workerIdBits;
	private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	private long sequenceMask = -1L ^ (-1L << sequenceBits);//序列号最大值
	private long lastTimestamp = -1L;
//	static {
//        try {
//            InputStream in = IdWorker.class.getClassLoader()
//                    .getResourceAsStream("application.properties");
//            Properties props = new Properties();
//            props.load(in);
//            workerId = Long.parseLong(props.getProperty("workerId", "0"));
//            datacenterId  = Long.parseLong(props.getProperty("centerId", "0"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("load wechat error " + e.getMessage());
//        }
//    }
	public IdWorker(long workerId_, long datacenterId_) {
		// sanity check for workerId
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format(
					"worker Id can't be greater than %d or less than 0",
					maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format(
					"datacenter Id can't be greater than %d or less than 0",
					maxDatacenterId));
		}
		workerId = workerId_;
		datacenterId = datacenterId_;
		log.info(String
				.format("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, workerid %d",
						timestampLeftShift, datacenterIdBits, workerIdBits,
						sequenceBits, workerId));
	}
	public IdWorker() {
		
	}
	public synchronized long nextId() {
		/*
		 * 获取系统时间戳
		 */
		long timestamp = timeGen();
		
		//防止系统时间出现错误
		if (timestamp < lastTimestamp) {
			log.warn(String.format(
					"clock is moving backwards. Rejecting requests until %d.",
					lastTimestamp));
			throw new RuntimeException(
					String.format(
							"Clock moved backwards. Refusing to generate id for %d milliseconds",
							lastTimestamp - timestamp));
		}
		/*
		 * 同一时间戳下，启用序列号自增，否则序列号初始化值为：0
		 */
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}
		//本次时间戳记录为最后一次的时间
		lastTimestamp = timestamp;
		return ((timestamp - twepoch) << timestampLeftShift)
				| (datacenterId << datacenterIdShift)
				| (workerId << workerIdShift) | sequence;
	}

	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	protected long timeGen() {
		return System.currentTimeMillis();
	}
}
