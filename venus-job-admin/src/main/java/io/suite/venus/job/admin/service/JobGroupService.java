package io.suite.venus.job.admin.service;

import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.core.model.XxlJobGroup;
import io.suite.venus.job.admin.domain.dto.GroupInfoReq;
import io.suite.venus.job.admin.domain.dto.UserInfo;
import io.suite.venus.job.admin.common.inout.BaseMngPageResp;



public interface JobGroupService {

    BaseMngPageResp<XxlJobGroup> getAllJobGroup(GroupInfoReq groupInfoReq, UserInfo userInfo);
    BaseMngPageResp<XxlJobGroup> getAllJobGroup(int start,int length, UserInfo userInfo);
    BaseResponse<String> save(XxlJobGroup xxlJobGroup);
    BaseResponse<String> update(XxlJobGroup xxlJobGroup);
    BaseResponse<String> remove(XxlJobGroup xxlJobGroup);
    XxlJobGroup getGroupById(int jobGroupId);

}
