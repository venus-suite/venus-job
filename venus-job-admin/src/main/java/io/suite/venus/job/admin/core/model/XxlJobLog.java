package io.suite.venus.job.admin.core.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * xxl-job log, used to track trigger process
 * @author xuxueli  2015-12-19 23:19:09
 */
@Data
public class XxlJobLog {
	
	private int id;
	
	// job info
	@ApiModelProperty(value = "执行器id")
	private int jobGroup;

	@ApiModelProperty(value = "任务ID")
	private int jobId;

	// execute info
	@ApiModelProperty(value = "执行地址")
	private String executorAddress;
	@ApiModelProperty(value = "执行handler")
	private String executorHandler;
	@ApiModelProperty(value = "执行参数")
	private String executorParam;
	@ApiModelProperty(value = "执行参数")
	private String executorShardingParam;
	@ApiModelProperty(value = "执行失败重试次数")
	private int executorFailRetryCount;
	
	// trigger info
	@ApiModelProperty(value = "执行时间")
	private Date triggerTime;
	@ApiModelProperty(value = "调度结果")
	private int triggerCode;

	@ApiModelProperty(value = "调度备注")
	private String triggerMsg;
	
	// handle info
	private Date handleTime;
	@ApiModelProperty(value = "执行结果")
	private int handleCode;
	@ApiModelProperty(value = "执行备注")
	private String handleMsg;

	// alarm info
	private int alarmStatus;

	@ApiModelProperty(value = "触发时间")
	private String triggerDay;

}
