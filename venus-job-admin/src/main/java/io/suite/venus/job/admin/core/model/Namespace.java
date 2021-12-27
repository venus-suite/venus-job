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
 * job对应的命名空间
 * </p>
 *
 * @author Shop
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "XxlJobQrtzNamespace对象", description = "job对应的命名空间")
@Entity
@Table(name = "xxl_job_qrtz_namespace")
public class Namespace implements Serializable {


    @ApiModelProperty(value = "主键")
    @TableId(value = "namespace_id", type = IdType.AUTO)
    @Id
    @Column(name = "namespace_id")
    private Long namespaceId;

    @ApiModelProperty(value = "业务名称")
    @Column(name = "business_name")
    private String businessName;

    @ApiModelProperty(value = "服务名")
    @Column(name = "service")
    private String service;

    @ApiModelProperty(value = "命名空间对应的key")
    @Column(name = "app_key")
    private String appKey;

    @ApiModelProperty(value = "空间描述")
    @Column(name = "remark")
    private String remark;

    @ApiModelProperty(value = "命名空间管理员，用来@对应的用户")
    @Column(name = "admin_user")
    private String adminUser;


    @ApiModelProperty(value = "创建时间")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "0=未删除，1=已删除")
    @Column(name = "is_deleted")
    private Integer isDeleted;


    @ApiModelProperty(value = "错误通知的web-hook（可以配置企业机器人)")
    @Column(name = "notice_web_hook")
    private String noticeWebHook;


    @ApiModelProperty(value = "审核通知的web-hook（可以配置企业机器人)")
    @Column(name = "audit_web_hook")
    private String auditWebHook;
}