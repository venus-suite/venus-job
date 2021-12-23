package io.suite.venus.job.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DetailReq {
    @ApiModelProperty("执行器地址")
    String executorAddress;
    @ApiModelProperty("触发时间")
    Long triggerTime;
    @ApiModelProperty("logId")
    Integer logId;
    @ApiModelProperty("行号")
    Integer fromLineNum;
}
