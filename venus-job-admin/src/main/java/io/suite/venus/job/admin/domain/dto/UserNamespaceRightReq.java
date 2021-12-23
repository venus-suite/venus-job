package io.suite.venus.job.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserNamespaceRightReq {
    @ApiModelProperty("用户邮箱")
    private String email;
    @ApiModelProperty("命名空间id")
    private  Long namespaceId;
}
