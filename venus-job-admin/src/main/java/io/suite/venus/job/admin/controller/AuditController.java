package io.suite.venus.job.admin.controller;

import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.common.log.ActionOperateLog;
import io.suite.venus.job.admin.controller.audit.AuditOpera;

import io.suite.venus.job.admin.domain.dto.*;
import io.suite.venus.job.admin.service.AuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;

@RestController
@Api(tags = "审核管理")
public class AuditController {

    @Autowired
    private AuditOpera auditOpera;
    @Autowired
    private AuditService auditService;

    @RequestMapping(value = "/job/audit/doAudit", method = RequestMethod.POST)
    @ApiOperation(value = "审核", notes = "审核管理")
    @ActionOperateLog(eventName = "审核管理[audit]",type = 0)
    public Object audit(@RequestBody AuditReq req) throws Exception {
        return auditOpera.executeAudit(req.getAuditId(), req.getRegCode());
    }


    @RequestMapping(value = "/job/audit/getAudioList", method = RequestMethod.POST)
    @ApiOperation(value = "定时任务审核列表", notes = "审核管理")
    @ActionOperateLog(eventName = "定时任务审核列表[getAudioList]",type = 0)
    public BaseResponse<BasePageResp<AuditRes>> getAudioList(@RequestBody AuditReq req) {
        UserInfo userInfo = UserInfoContext.getUserInfo();
        buildQueryData(req);
        return BaseResponse.valueOfSuccess(auditService.getAuditPageList(req, userInfo));
    }

    @RequestMapping(value = "/audit/getAudioDetail", method = RequestMethod.POST)
    @ApiOperation(value = "定时任务审核详情", notes = "审核管理")
    @ActionOperateLog(eventName = "定时任务审核详情[getAudioDetail]",type = 0)
    public BaseResponse<AuditRes> getAudioDetail(@RequestBody AuditReq req) {
        UserInfo userInfo = UserInfoContext.getUserInfo();
        return BaseResponse.valueOfSuccess(auditService.getAudioDetail(req, userInfo));
    }

    private  void buildQueryData(AuditReq req) {
        if (!CollectionUtils.isEmpty(req.getCreateTime())) {
            if (req.getCreateTime().size() == 2) {
                String format = "yyyy-MM-dd HH:mm:ss";
                String startTime = req.getCreateTime().get(0);
                String endTime = req.getCreateTime().get(1);
                try {
                    Date d1 = DateUtils.parseDate(startTime, format);
                    Date d2 = DateUtils.parseDate(endTime, format);
                    req.setApplyStartTime(d1.getTime());
                    req.setApplyEndTime(d2.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
