package io.suite.venus.job.admin.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * VenusJobQrtzUser 用户表.
 *
 * @author peiyy
 * @version 1.0
 * @date 2021-12-17 14:55
 */
@Getter
@Setter
@ApiModel("分页查询用户")
public class VenusJobUserQueryReq extends BaseReq{
private static final long serialVersionUID=1L;

    /**
     * 企业邮箱(帐号)
     */
    @ApiModelProperty(value = "企业邮箱(帐号)")
    private String email;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String name;


    /**
     * 是否超级管理员，0否，1是
     */
    @ApiModelProperty(value = "是否超级管理员，0否，1是")
    private Integer isSuper;

    @ApiModelProperty(hidden = true)
    private Integer isDeleted = 0;
}