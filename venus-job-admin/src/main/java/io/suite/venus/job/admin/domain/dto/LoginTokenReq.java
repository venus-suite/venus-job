package io.suite.venus.job.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginTokenReq {
    @ApiModelProperty(value = "用户登录token",required = true)
    @NotBlank(message = "用户登录token不能为空")
    private String token;
}
