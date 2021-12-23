package io.suite.venus.job.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GroupInfoReq extends  BaseReq {

    @ApiModelProperty("AppName")
    private  String appName;

    @ApiModelProperty("名称")
    private  String title;

    @ApiModelProperty("注册方式 (0:自动注册 1:手动录入) ")
    private Integer addressType;

    @ApiModelProperty("namespace ")
    private String namespace;
    @ApiModelProperty("namespaceId")
    private  Integer namespaceId;

}
