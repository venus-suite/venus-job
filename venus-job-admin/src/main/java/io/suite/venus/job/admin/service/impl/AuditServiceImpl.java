package io.suite.venus.job.admin.service.impl;

import com.alibaba.fastjson.JSON;
import io.suite.venus.job.admin.common.util.AssertUtil;
import io.suite.venus.job.admin.core.model.Audit;
import io.suite.venus.job.admin.core.model.AuditDetail;
import io.suite.venus.job.admin.domain.dto.AuditReq;
import io.suite.venus.job.admin.domain.dto.AuditRes;
import io.suite.venus.job.admin.domain.dto.BasePageResp;
import io.suite.venus.job.admin.domain.dto.UserInfo;
import io.suite.venus.job.admin.mapper.AuditDetailMapper;
import io.suite.venus.job.admin.mapper.AuditMapper;
import io.suite.venus.job.admin.service.AuditService;
import io.suite.venus.job.admin.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuditServiceImpl implements AuditService {
    @Autowired
    private AuditMapper auditMapper;

    @Autowired
    private AuditDetailMapper auditDetailMapper;

    @Autowired
    private UserService userService;

    public BasePageResp<AuditRes> getAuditPageList(AuditReq req, UserInfo userInfo) {
        BasePageResp<AuditRes> basePageResp = new BasePageResp();
        try {
            if (userService.isSuperAdmin(userInfo.getEmail())) {
                req.setIsAdmin(1);
            } else {
                req.setIsAdmin(0);
            }
            req.setEmail(userInfo.getEmail());

            List<AuditRes> resultList = new ArrayList<>();
            PageHelper.startPage(req.getCurrent(), req.getPageSize());
            List<Audit> result = auditMapper.getAuditPageList(req);
            if (!CollectionUtils.isEmpty(result)) {
                List<Long> idList = new ArrayList<>();
                for (Audit audit : result) {
                    AuditRes res = new AuditRes();
                    BeanUtils.copyProperties(audit, res);
                    idList.add(audit.getAuditId());
                    resultList.add(res);
                }
                List<AuditDetail> auditDetails = auditMapper.getAudioDetailByIdList(idList);
                if (!CollectionUtils.isEmpty(auditDetails)) {
                    Map<Long, AuditDetail> detailMap = auditDetails.stream().collect(Collectors.toMap(AuditDetail::getAuditId, t -> t));

                    for (AuditRes audit : resultList) {
                        AuditDetail detail = detailMap.get(audit.getAuditId());
                        if (detail != null) {
                            audit.setDetail(JSON.parseObject(detail.getAuditText()));
                        }
                    }
                }

            }
            PageInfo<Audit> pageInfo = new PageInfo<>(result);
            basePageResp.setTotal(pageInfo.getTotal());
            basePageResp.setPages(pageInfo.getPages());
            basePageResp.setContentList(resultList);
        } finally {
            PageHelper.clearPage();
        }
        return basePageResp;
    }

    @Override
    public AuditRes getAudioDetail(AuditReq req, UserInfo userInfo) {
        AuditRes res = new AuditRes();
        Audit audit = auditMapper.selectByPrimaryKey(req.getAuditId());
        AuditDetail auditDetail = auditDetailMapper.selectByPrimaryKey(req.getAuditId());
        AssertUtil.isTrue(audit != null, "获取审核数据失败");
        AssertUtil.isTrue(auditDetail != null, "获取审核详情数据失败");
        BeanUtils.copyProperties(audit, res);
        res.setDetail(JSON.parseObject(auditDetail.getAuditText()));
        return res;
    }

}
