package io.suite.venus.job.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@Data
public class NamespaceReq  extends  BaseReq{

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long namespaceId;

    /**
     * 命名空间
     */
    @ApiModelProperty(value = "业务名称")
    private String businessName;

    /**
     * 服务名
     */
    @ApiModelProperty(value = "服务名")
    private String service;

    /**
     * 命名空间对应的key
     */
    @ApiModelProperty(value = "命名空间对应的key")
    private String appKey;


    @ApiModelProperty(value = "企业机器人")
    @Column(name = "notice_web_hook")
    private String noticeWebHook;

    @ApiModelProperty(value = "审核通知的web-hook（可以配置企业机器人)")
    private String auditWebHook;

    /**
     * 空间描述
     */
    private String remark;

    /**
     * 邮箱
     */
    private  String email;

}
