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
}
