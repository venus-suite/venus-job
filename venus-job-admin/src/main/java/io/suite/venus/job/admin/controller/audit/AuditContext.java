package io.suite.venus.job.admin.controller.audit;

import io.suite.venus.job.admin.core.model.Audit;
import io.suite.venus.job.admin.core.model.Namespace;
import io.suite.venus.job.admin.domain.dto.AuditDetailReq;
import io.suite.venus.job.admin.domain.dto.UserInfo;
import lombok.Data;

@Data
public class AuditContext {
    private String code;
    private AuditDetailReq detail;
    private Namespace namespace;
    private long targetId;
    private Audit audit;
    private UserInfo userInfo;
}
