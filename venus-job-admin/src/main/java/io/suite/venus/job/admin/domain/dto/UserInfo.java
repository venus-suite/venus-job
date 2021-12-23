package io.suite.venus.job.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfo {

    @ApiModelProperty("userId")
    private Long userId=0l;

    @ApiModelProperty("email")
    private String email;

    @ApiModelProperty("userName")
    private String userName;

    @ApiModelProperty("userToken")
    private String token;
}
