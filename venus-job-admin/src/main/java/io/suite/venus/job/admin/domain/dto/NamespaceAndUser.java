package io.suite.venus.job.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class NamespaceAndUser {

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


    @ApiModelProperty(value = "是否超级管理员（true是  false 否")
    @Column(name = "isAdmin")
    private  boolean isAdmin;

    @ApiModelProperty(value = "拥有这个命名空间的用户")
    private List<UserNamespace> userNamespaceList;

    @ApiModelProperty(value = "错误通知的web-hook（可以配置企业机器人)")
    private String noticeWebHook;

    @ApiModelProperty(value = "审核通知的web-hook（可以配置企业机器人)")
    private String auditWebHook;

    @ApiModelProperty(value = "描述")
    private String remark;




}
