package io.suite.venus.job.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginReq {
    @ApiModelProperty(value = "用户账号或邮箱",required = true)
    @NotBlank(message = "用户账号或邮箱不能为空")
    private String userAccount;

    @ApiModelProperty(value = "用户密码",required = true)
    @NotBlank(message = "用户密码不能为空")
    private String password;
}
