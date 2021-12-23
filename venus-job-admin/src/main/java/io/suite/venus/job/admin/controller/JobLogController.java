package io.suite.venus.job.admin.controller;

import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.common.log.ActionOperateLog;
import io.suite.venus.job.admin.core.model.XxlJobGroup;
import io.suite.venus.job.admin.core.model.XxlJobInfo;
import io.suite.venus.job.admin.core.model.XxlJobLog;
import io.suite.venus.job.admin.core.schedule.XxlJobDynamicScheduler;
import io.suite.venus.job.admin.core.util.I18nUtil;
import io.suite.venus.job.admin.domain.dto.*;
import io.suite.venus.job.admin.common.inout.BaseMngPageResp;
import io.suite.venus.job.admin.mapper.XxlJobInfoMapper;
import io.suite.venus.job.admin.mapper.XxlJobLogMapper;
import io.suite.venus.job.admin.service.JobGroupService;
import io.suite.venus.job.admin.service.LogService;
import io.suite.venus.job.admin.service.UserService;
import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.model.LogResult;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
@RestController
@RequestMapping("/job/joblog")
@Api(tags = "任务日志")
public class JobLogController {
    private static Logger logger = LoggerFactory.getLogger(JobLogController.class);

    @Resource
    private JobGroupService jobGroupService;

    @Resource
    private LogService logService;

    @Resource
    private UserService userService;
    @Resource
    public XxlJobInfoMapper xxlJobInfoMapper;
    @Resource
    public XxlJobLogMapper xxlJobLogMapper;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value = "日志列表枚举相关", notes = "任务日志")
    public BaseResponse<Map<String, Object>> index(@RequestHeader("token") String userToken, @RequestParam(required = false, defaultValue = "0") Integer jobId) {
        Map<String, Object> model = new HashMap<>();
        // 执行器列表
        UserInfo userInfo = userService.getUserInfoById(userToken);
        BaseMngPageResp<XxlJobGroup> jobGroupList = jobGroupService.getAllJobGroup(1, 1000, userInfo);
        model.put("JobGroupList", jobGroupList.getContentList());

        // 任务
        if (jobId > 0) {
            XxlJobInfo jobInfo = xxlJobInfoMapper.loadById(jobId);
            model.put("jobInfo", jobInfo);
        }

        return BaseResponse.valueOfSuccess(model);
    }

    @RequestMapping(value = "/getJobsByGroup", method = RequestMethod.POST)
    @ApiOperation(value = "获取任务分组", notes = "任务日志")
    public ReturnT<List<XxlJobInfo>> getJobsByGroup(int jobGroup) {
        List<XxlJobInfo> list = xxlJobInfoMapper.getJobsByGroup(jobGroup);
        return new ReturnT<List<XxlJobInfo>>(list);
    }

    @RequestMapping(value = "/pageList", method = RequestMethod.POST)
    @ApiOperation(value = "日志列表分页", notes = "任务日志")
    public BaseResponse<BaseMngPageResp<JobLogRes>> pageList(@RequestBody LogInfoReq req) {
        // page query
        ///BaseMngPageResp<XxlJobLog> list = logService.pageList(req);
        //int list_count = xxlJobLogMapper.pageListCount(start, length, jobGroup, jobId, triggerTimeStart, triggerTimeEnd, logStatus);
        UserInfo userInfo = UserInfoContext.getUserInfo();
        String email = userInfo.getEmail();
        if (userService.isSuperAdmin(userInfo.getEmail())) {
            email = "";
        }
        req.setEmail(email);
        BaseMngPageResp<JobLogRes> list = logService.pageList(req);
        return BaseResponse.valueOfSuccess(list);// 分页列表

    }

    @RequestMapping(value = "/logDetailPage", method = RequestMethod.POST)
    @ApiOperation(value = "执行日志", notes = "任务日志")
    public XxlJobLog logDetailPage(int id, Model model) {
        // base check
        ReturnT<String> logStatue = ReturnT.SUCCESS;
        XxlJobLog jobLog = xxlJobLogMapper.load(id);
        if (jobLog == null) {
            throw new RuntimeException(I18nUtil.getString("joblog_logid_unvalid"));
        }
        return jobLog;
    }

    @RequestMapping(value = "/logDetailCat", method = RequestMethod.POST)
    @ApiOperation(value = "日志详情", notes = "任务日志")
    @ActionOperateLog(eventName = "日志详情[logDetailCat]",type = 0)
    public ReturnT<LogResult> logDetailCat(@RequestBody DetailReq req) {
        try {
            String executorAddress=req.getExecutorAddress();
            long triggerTime=req.getTriggerTime();
            int logId=req.getLogId();
            int fromLineNum= req.getFromLineNum();
            ExecutorBiz executorBiz = XxlJobDynamicScheduler.getExecutorBiz(executorAddress);
            ReturnT<LogResult> logResult = executorBiz.log(triggerTime, logId, fromLineNum);

            // is end
            if (logResult.getContent() != null && logResult.getContent().getFromLineNum() > logResult.getContent().getToLineNum()) {
                XxlJobLog jobLog = xxlJobLogMapper.load(logId);
                if (jobLog.getHandleCode() > 0) {
                    logResult.getContent().setEnd(true);
                }

            }
            if (logResult.getContent() != null) {
                String txt = logResult.getContent().getLogContent();
                if (!StringUtils.isEmpty(txt)) {
                    txt = txt.replace("\n", "<br/>");
                    logResult.getContent().setLogContent(txt);
                }
            }

            return logResult;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ReturnT<LogResult>(ReturnT.FAIL_CODE, e.getMessage());
        }
    }

    @RequestMapping(value = "/logKill", method = RequestMethod.POST)
    @ApiOperation(value = "日志kill", notes = "任务日志")
    @ActionOperateLog(eventName = "日志kill[logKill]",type = 0)
    public ReturnT<String> logKill(int id) {
        // base check
        XxlJobLog log = xxlJobLogMapper.load(id);
        XxlJobInfo jobInfo = xxlJobInfoMapper.loadById(log.getJobId());
        if (jobInfo == null) {
            return new ReturnT<String>(500, I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
        }
        if (ReturnT.SUCCESS_CODE != log.getTriggerCode()) {
            return new ReturnT<String>(500, I18nUtil.getString("joblog_kill_log_limit"));
        }

        // request of kill
        ReturnT<String> runResult = null;
        try {
            ExecutorBiz executorBiz = XxlJobDynamicScheduler.getExecutorBiz(log.getExecutorAddress());
            runResult = executorBiz.kill(jobInfo.getId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            runResult = new ReturnT<String>(500, e.getMessage());
        }

        if (ReturnT.SUCCESS_CODE == runResult.getCode()) {
            log.setHandleCode(ReturnT.FAIL_CODE);
            log.setHandleMsg(I18nUtil.getString("joblog_kill_log_byman") + ":" + (runResult.getMsg() != null ? runResult.getMsg() : ""));
            log.setHandleTime(new Date());
            xxlJobLogMapper.updateHandleInfo(log);
            return new ReturnT<String>(runResult.getMsg());
        } else {
            return new ReturnT<String>(500, runResult.getMsg());
        }
    }

    @RequestMapping(value = "/clearLog", method = RequestMethod.POST)
    @ApiOperation(value = "清空日志", notes = "任务日志")
    @ActionOperateLog(eventName = "清空日志[clearLog]",type = 0)
    public ReturnT<String> clearLog(int jobGroup, int jobId, int type) {

        Date clearBeforeTime = null;
        int clearBeforeNum = 0;
        if (type == 1) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -1);    // 清理一个月之前日志数据
        } else if (type == 2) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -3);    // 清理三个月之前日志数据
        } else if (type == 3) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -6);    // 清理六个月之前日志数据
        } else if (type == 4) {
            clearBeforeTime = DateUtil.addYears(new Date(), -1);    // 清理一年之前日志数据
        } else if (type == 5) {
            clearBeforeNum = 1000;        // 清理一千条以前日志数据
        } else if (type == 6) {
            clearBeforeNum = 10000;        // 清理一万条以前日志数据
        } else if (type == 7) {
            clearBeforeNum = 30000;        // 清理三万条以前日志数据
        } else if (type == 8) {
            clearBeforeNum = 100000;    // 清理十万条以前日志数据
        } else if (type == 9) {
            clearBeforeNum = 0;            // 清理所有日志数据
        } else {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("joblog_clean_type_unvalid"));
        }

        xxlJobLogMapper.clearLog(jobGroup, jobId, clearBeforeTime, clearBeforeNum);
        return ReturnT.SUCCESS;
    }

}
