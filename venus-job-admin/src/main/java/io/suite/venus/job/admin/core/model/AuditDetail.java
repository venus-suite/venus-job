package io.suite.venus.job.admin.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 定时任务操作审核
 * </p>
 *
 * @author Shop
 * @since 2021-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "XxlJobQrtzAuditDetail对象", description = "定时任务操作审核")
@Entity
@Table(name = "xxl_job_qrtz_audit_detail")
public class AuditDetail implements Serializable {


    @ApiModelProperty(value = "主键")
    @TableId(value = "audit_detail_id", type = IdType.AUTO)
    @Id
    @Column(name = "audit_detail_id")
    private Long auditDetailId;

    @ApiModelProperty(value = "审核id")
    @Column(name = "audit_id")
    private Long auditId;

    @ApiModelProperty(value = "操作内容")
    @Column(name = "audit_text")
    private String auditText;

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
