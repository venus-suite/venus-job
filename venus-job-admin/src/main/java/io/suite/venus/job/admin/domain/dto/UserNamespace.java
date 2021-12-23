package io.suite.venus.job.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserNamespace {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long namespaceId;

    /**
     * 命名空间
     */
    @ApiModelProperty(value = "命名空间")
    private String namespace;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty("是否需要审核操作（0:否 1 是）  默认都是需要审核 ")
    private Integer needAudit=0;

}
