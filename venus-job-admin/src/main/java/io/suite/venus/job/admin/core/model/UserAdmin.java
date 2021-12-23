package io.suite.venus.job.admin.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * job管理超级管理员
 * </p>
 *
 * @author Shop
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "XxlJobQrtzUserAdmin对象", description = "job管理超级管理员")
@Entity
@Table(name = "xxl_job_qrtz_user_admin")
public class  UserAdmin implements Serializable {


    @ApiModelProperty(value = "主键")
    @TableId(value = "admin_id", type = IdType.AUTO)
    @Id
    @Column(name = "admin_id")
    private Long adminId;

    @ApiModelProperty(value = "管理员邮箱")
    @Column(name = "email")
    private String email;

    @ApiModelProperty(value = "创建时间")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "0=未删除，1=已删除")
    @Column(name = "is_deleted")
    private Integer isDeleted;


}
