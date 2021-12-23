package io.suite.venus.job.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AuditReq extends  BaseReq {
    @ApiModelProperty(hidden = true)
    private long userId;
    @ApiModelProperty(hidden = true)
    private String email;
    @ApiModelProperty(hidden = true)
    private int isAdmin = 0;
    @ApiModelProperty("审核状态(1,-未审核  2-已审核")
    private int status;
    @ApiModelProperty("审核Id")
    private Long auditId;
    @ApiModelProperty("验证码")
    private String regCode;
    @ApiModelProperty(value = "操作描述")
    private String auditDesc;
    @ApiModelProperty(value = "操作描述")
    private List<String> createTime;
    private  Long applyStartTime;
    private  Long applyEndTime;


}
