package io.suite.venus.job.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class JobInfoReq extends  BaseReq {

    @ApiModelProperty(value = "id")
    private int id;
    @ApiModelProperty(value = "执行器")
    private int jobGroup;
    @ApiModelProperty(value = "任务描述")
    private String jobDesc;
    @ApiModelProperty(value = "JobHandler")
    private String executorHandler;
    @ApiModelProperty(value = "时间过滤")
    private String filterTime;
    @ApiModelProperty(value = "状态")
    private String jobStatus;
    @ApiModelProperty(value = "author")
    private String author;
    @ApiModelProperty(value = "jobCron")
    private String jobCron;
}
