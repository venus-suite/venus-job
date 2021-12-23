package io.suite.venus.job.admin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * xxl-job info
 *
 * @author xuxueli  2016-1-12 18:25:49
 */
@ApiModel("job任务日志")
@Data
public class JobLogRes {
	private int id;

	// job info
	@ApiModelProperty(value = "执行器id")
	private int jobGroup;

	@ApiModelProperty(value = "任务ID")
	private int jobId;

	@ApiModelProperty(value = "任务名称")
	private String jobName;

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
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date triggerTime;
	@ApiModelProperty(value = "调度结果")
	private int triggerCode;

	@ApiModelProperty(value = "调度备注")
	private String triggerMsg;

	// handle info
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@ApiModelProperty(value = "执行时间")
	private Date handleTime;
	@ApiModelProperty(value = "执行结果")
	private int handleCode;
	@ApiModelProperty(value = "执行备注")
	private String handleMsg;

	// alarm info
	private int alarmStatus;



}
