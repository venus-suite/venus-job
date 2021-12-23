package io.suite.venus.job.admin.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author author
 * @since 2021-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@ApiModel(value="VenusJobQrtzUser对象", description="用户表")
public class VenusJobQrtzUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    @Id
    private Long id;

    @ApiModelProperty(value = "企业邮箱(帐号)")
    private String email;

    @ApiModelProperty(value = "真实姓名")
    private String name;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "登录token")
    private String token;

    @ApiModelProperty(value = "token生成时间,方便计算过期时间，存毫秒时间戳")
    private Long tokenTime;

    @ApiModelProperty(value = "最后操作人id")
    private Long operatorId;

    @ApiModelProperty(value = "最后操作人")
    private String operator;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "0:有效，1:删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "最后的登录时间")
    private Date lastLoginTime;

    @ApiModelProperty(value = "是否超级管理员，0否，1是")
    private Integer isSuper;


}
