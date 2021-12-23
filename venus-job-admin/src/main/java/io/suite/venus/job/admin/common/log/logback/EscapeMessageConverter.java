package io.suite.venus.job.admin.common.log.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.alibaba.fastjson.JSON;
import org.slf4j.MDC;

import java.util.Date;

public class EscapeMessageConverter extends ClassicConverter {

	
	public String convert(ILoggingEvent event) {
		String message = event.getFormattedMessage();
		LogbackMsgInfo logbackMsgInfo = new LogbackMsgInfo();
		logbackMsgInfo.setTime(new Date());
		logbackMsgInfo.setLevel(event.getLevel().toString());
		logbackMsgInfo.setThread(event.getThreadName());
		logbackMsgInfo.setTraceId(MDC.get("req.traceId"));
		logbackMsgInfo.setUrl(MDC.get("req.requestURL"));
		logbackMsgInfo.setUserAgent(MDC.get("req.userAgent"));
		if(message!=null&&message.contains("--")){
			String[] sts=message.split("--");
			if(sts.length>0){
				logbackMsgInfo.setTitle(sts[0]);
			}
		}
		logbackMsgInfo.setMsg(message);
		String json=JSON.toJSONString(logbackMsgInfo);
		return json;
	}

}
