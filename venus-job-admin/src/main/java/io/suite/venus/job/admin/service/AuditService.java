package io.suite.venus.job.admin.service;

import io.suite.venus.job.admin.domain.dto.AuditReq;
import io.suite.venus.job.admin.domain.dto.AuditRes;
import io.suite.venus.job.admin.domain.dto.BasePageResp;
import io.suite.venus.job.admin.domain.dto.UserInfo;

public interface AuditService {
    BasePageResp<AuditRes>  getAuditPageList(AuditReq req, UserInfo userInfo);

    AuditRes getAudioDetail(AuditReq req, UserInfo userInfo);

}
