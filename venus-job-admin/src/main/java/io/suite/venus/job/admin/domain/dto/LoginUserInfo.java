package io.suite.venus.job.admin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class LoginUserInfo {

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("email")
    private String email;

    @ApiModelProperty("userName")
    private String userName;

    @ApiModelProperty("是否超级管理员，0否1是")
    private Integer isSuper;

    @ApiModelProperty(value = "上次登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;
}
