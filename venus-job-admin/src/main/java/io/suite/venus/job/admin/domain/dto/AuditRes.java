package io.suite.venus.job.admin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

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

public class AuditRes implements Serializable {


    @ApiModelProperty(value = "主键")

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


    @ApiModelProperty(value = "是否有效（1,-有效 0-无效）")
    @Column(name = "status")
    private Integer status;

    @ApiModelProperty(value = "申请时间")
    @Column(name = "apply_time")
    private Long applyTime;

    @ApiModelProperty(value = "创建时间")
    @Column(name = "create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "提交详情")
    private Map<String,Object> detail;

    @ApiModelProperty(value = "提交人email")
    private String email;

    @ApiModelProperty(value = "提交人姓名")
    private String userName;

}
