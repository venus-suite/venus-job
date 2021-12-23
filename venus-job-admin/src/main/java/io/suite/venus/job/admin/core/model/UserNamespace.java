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
 *
 * </p>
 *
 * @author Shop
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "XxlJobQrtzUserNamespace对象", description = "")
@Entity
@Table(name = "xxl_job_qrtz_user_namespace")
public class  UserNamespace implements Serializable {


    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(value = "用户id")
    @Column(name = "user_id")
    private Long userId;

    @ApiModelProperty(value = "命名空间")
    @Column(name = "namespace_id")
    private Long namespaceId;

    @ApiModelProperty(value = "用户邮箱")
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

    @ApiModelProperty(value = "是否需要审核(发消息）（1-需要 0 -不需要)")
    @Column(name = "need_audit")
    private Integer needAudit=0;


}
