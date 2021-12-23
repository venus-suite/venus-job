package io.suite.venus.job.admin.service.impl;

import io.suite.venus.job.admin.core.model.XxlJobInfo;
import io.suite.venus.job.admin.core.model.XxlJobLog;
import io.suite.venus.job.admin.core.model.XxlJobServerInfoHis;
import io.suite.venus.job.admin.domain.dto.*;
import io.suite.venus.job.admin.common.inout.BaseMngPageResp;
import io.suite.venus.job.admin.common.inout.ServerInfoReq;
import io.suite.venus.job.admin.mapper.XxlJobInfoMapper;
import io.suite.venus.job.admin.mapper.XxlJobLogMapper;
import io.suite.venus.job.admin.mapper.XxlJobServerInfoHisMapper;
import io.suite.venus.job.admin.service.LogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxl.job.core.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class LogServiceImpl  implements LogService {
    @Autowired
    private XxlJobLogMapper xxlJobLogMapper;
    @Autowired
    private XxlJobInfoMapper xxlJobInfoMapper;

    @Autowired
    private XxlJobServerInfoHisMapper xxlJobServerInfoHisMapper;


    @Override
    public BaseMngPageResp<JobLogRes> pageList(LogInfoReq req) {

        int start = req.getCurrent();
        int length = req.getPageSize();
        int jobGroup = req.getJobGroup();
        int jobId = req.getJobId();
        int logStatus = req.getLogStatus();

        // parse param
        Date triggerTimeStart = null;
        Date triggerTimeEnd = null;
        List<String> triggerTime = req.getTriggerTime();
        if (triggerTime != null && triggerTime.size() == 2) {
            triggerTimeStart = DateUtil.parseDateTime(triggerTime.get(0));
            triggerTimeEnd = DateUtil.parseDateTime(triggerTime.get(1));

        }

        try {
            PageHelper.startPage(start, length);
            List<XxlJobLog> list = xxlJobLogMapper.pageList(start, length, jobGroup, jobId, triggerTimeStart, triggerTimeEnd,
                    logStatus,req.getEmail());

            List<JobLogRes> retList = new ArrayList<>();
            PageInfo<XxlJobLog> pageInfo = new PageInfo<>(list);
            BaseMngPageResp<JobLogRes> resp = new BaseMngPageResp<>();
            resp.setTotal(pageInfo.getTotal());
            resp.setPages(pageInfo.getPages());
            if (!CollectionUtils.isEmpty(list)) {
                List<Integer> jobIdList = new ArrayList<>();
                for (XxlJobLog log : list) {
                    jobIdList.add(log.getJobId());
                }
                Map<Integer, XxlJobInfo> jobInfoMap = new HashMap<>();
                List<XxlJobInfo> jobInfoList = xxlJobInfoMapper.getJobsByIdList(jobIdList);
                if (!CollectionUtils.isEmpty(jobIdList)) {
                    jobInfoList.stream().forEach(item -> {
                        jobInfoMap.put(item.getId(), item);
                    });
                }
                for (XxlJobLog log : list) {
                    JobLogRes res = new JobLogRes();
                    BeanUtils.copyProperties(log, res);
                    XxlJobInfo jobInfo = jobInfoMap.get(res.getJobId());
                    if (jobInfo != null) {
                        res.setJobName(jobInfo.getJobDesc());
                    }
                    retList.add(res);
                }
            }
            resp.setContentList(retList);
            return resp;
        } finally {
            PageHelper.clearPage();
        }

    }

    @Override
    public BaseMngPageResp<OperaLogRes> getOperLogPageList(BaseReq req) {
        int start = req.getCurrent();
        int length = req.getPageSize();

        try {
            PageHelper.startPage(start, length);
            List<OperaLogRes> list = xxlJobLogMapper.getOperLogPageList(start, length);
            PageInfo<OperaLogRes> pageInfo = new PageInfo<>(list);
            BaseMngPageResp<OperaLogRes> resp = new BaseMngPageResp<>();
            resp.setTotal(pageInfo.getTotal());
            resp.setPages(pageInfo.getPages());
            resp.setContentList(list);
            return resp;
        } finally {
            PageHelper.clearPage();
        }
    }

    @Override
    public List<XxlJobServerInfoHis> getServerHisPage(ServerInfoReq req, UserInfo userInfo) {
        List<XxlJobServerInfoHis> ret= xxlJobServerInfoHisMapper.getServerHisList(req);
        return ret;
    }
}
