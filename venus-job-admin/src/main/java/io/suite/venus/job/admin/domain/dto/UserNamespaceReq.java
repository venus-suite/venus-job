package io.suite.venus.job.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserNamespaceReq {
   @ApiModelProperty("命名空间ID")
   private List<Long> namespaceIdList;

   @ApiModelProperty("用户邮箱")
   private List<String> emailList;

   @ApiModelProperty("是否需要审核操作（0:否 1 是）  默认都是需要审核 ")
   private Integer needAudit=1;
}
