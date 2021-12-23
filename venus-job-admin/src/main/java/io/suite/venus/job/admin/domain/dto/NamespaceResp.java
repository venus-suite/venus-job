package io.suite.venus.job.admin.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
public class NamespaceResp {
    @ApiModelProperty(value = "主键")
    @TableId(value = "namespace_id", type = IdType.AUTO)
    @Id
    @Column(name = "namespace_id")
    private Long namespaceId;

    @ApiModelProperty(value = "命名空间")
    @Column(name = "namespace")
    private String namespace;

}
