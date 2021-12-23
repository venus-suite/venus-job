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
 * 定时任务操作审核
 * </p>
 *
 * @author Shop
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "XxlJobQrtzAudit对象", description = "定时任务操作审核")
@Entity
@Table(name = "xxl_job_qrtz_audit")
public class  Audit implements Serializable {


    @ApiModelProperty(value = "主键")
    @TableId(value = "audit_id", type = IdType.AUTO)
    @Id
    @Column(name = "audit_id")
    private Long auditId;

    @ApiModelProperty(value = "操作编码（每个操作不同的code）")
    @Column(name = "audit_code")
    private String auditCode;


    @ApiModelProperty(value = "操作描述")
    @Column(name = "audit_desc")
    private String auditDesc;

    @ApiModelProperty(value = "操作编码（操作编码对应的操作类型id）")
    @Column(name = "audit_source_id")
    private Long auditSourceId;


    @ApiModelProperty(value = "是否审核（1,-有效  2-无效(已经审核））")
    @Column(name = "status")
    private Integer status;

    @ApiModelProperty(value = "申请时间")
    @Column(name = "apply_time")
    private Long applyTime;

    @ApiModelProperty(value = "创建时间")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "0=未删除，1=已删除")
    @Column(name = "is_deleted")
    private Integer isDeleted;

    @ApiModelProperty(value = "验证码")
    @Column(name = "verification_code")
    private String verificationCode;

    @ApiModelProperty(value = "发送验证码的用户id")
    @Column(name = "user_id")
    private Long userId;

    @ApiModelProperty(value = "'命名空间'")
    @Column(name = "namespace")
    private String namespace;

    @ApiModelProperty(value = "'命名空间Id")
    @Column(name = "namespace_id")
    private Long namespaceId;



    @ApiModelProperty(value = "提交人email")
    private String email;

    @ApiModelProperty(value = "提交人姓名")
    private String userName;


}
