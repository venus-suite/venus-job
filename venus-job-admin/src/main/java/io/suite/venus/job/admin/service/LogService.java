package io.suite.venus.job.admin.service;

import io.suite.venus.job.admin.core.model.XxlJobServerInfoHis;
import io.suite.venus.job.admin.domain.dto.*;
import io.suite.venus.job.admin.common.inout.BaseMngPageResp;
import io.suite.venus.job.admin.common.inout.ServerInfoReq;

import java.util.List;

public interface LogService {
    BaseMngPageResp<JobLogRes> pageList(LogInfoReq req);
    BaseMngPageResp<OperaLogRes> getOperLogPageList(BaseReq req);

    List<XxlJobServerInfoHis> getServerHisPage(ServerInfoReq request, UserInfo userInfo);
}
