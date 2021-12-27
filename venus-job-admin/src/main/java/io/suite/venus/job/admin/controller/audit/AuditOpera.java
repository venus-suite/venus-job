package io.suite.venus.job.admin.controller.audit;

import com.alibaba.fastjson.JSON;
import io.suite.venus.job.admin.common.exception.BizBaseException;
import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.common.util.AssertUtil;
import io.suite.venus.job.admin.core.model.*;
import io.suite.venus.job.admin.core.util.IdWorker;
import io.suite.venus.job.admin.core.util.RandomUtil;
import io.suite.venus.job.admin.domain.dto.AuditDetailReq;
import io.suite.venus.job.admin.domain.dto.UserInfo;
import io.suite.venus.job.admin.domain.dto.UserInfoContext;
import io.suite.venus.job.admin.enums.AuditStatusEnum;
import io.suite.venus.job.admin.enums.AuditTypeEnum;
import io.suite.venus.job.admin.mapper.AuditDetailMapper;
import io.suite.venus.job.admin.mapper.AuditMapper;
import io.suite.venus.job.admin.service.JobGroupService;
import io.suite.venus.job.admin.service.XxlJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Component
public class AuditOpera {
    @Autowired
    private AuditMapper auditMapper;
    @Autowired
    private AuditDetailMapper auditDetailMapper;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private IdWorker idWorker;

    /**
     * 写入审核数据
     *
     * @param auditContext
     * @return
     */
    public BaseResponse<Boolean> addAuditData(AuditContext auditContext) {
        Audit audit = new Audit();
        audit.setAuditId(idWorker.nextId());
        audit.setApplyTime(System.currentTimeMillis());
        audit.setAuditCode(auditContext.getCode());
        audit.setUpdateTime(LocalDateTime.now());
        audit.setStatus(AuditStatusEnum.YES.getCode());
        audit.setAuditDesc(auditContext.getDetail().getDesc());
        audit.setVerificationCode(RandomUtil.getRandomCode());
        Namespace namespace=auditContext.getNamespace();
        audit.setNamespace(namespace.getBusinessName()+":"+namespace.getService());
        audit.setNamespaceId(namespace.getNamespaceId());
        audit.setUserId(UserInfoContext.getUserInfo().getUserId());
        audit.setAuditSourceId(auditContext.getTargetId());
        audit.setCreateTime(LocalDateTime.now());
        audit.setUserName(auditContext.getUserInfo().getUserName());
        audit.setEmail(auditContext.getUserInfo().getEmail());
        audit.setIsDeleted(0);
        auditMapper.insertSelective(audit);
        Long auditId = audit.getAuditId();
        AuditDetail detail = new AuditDetail();
        detail.setAuditId(auditId);
        detail.setAuditDetailId(auditId);
        detail.setAuditText(JSON.toJSONString(auditContext.getDetail()));
        detail.setIsDeleted(0);
        detail.setCreateTime(LocalDateTime.now());
        detail.setUpdateTime(LocalDateTime.now());
        auditContext.setAudit(audit);
        return BaseResponse.valueOfSuccess(auditDetailMapper.insertSelective(detail) > 0);
    }

    /**
     * 处理执行审核
     *
     * @param auditId
     * @param regCode
     * @return
     */
    public Object executeAudit(Long auditId, String regCode) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        UserInfo userInfo = UserInfoContext.getUserInfo();
        Audit audit = auditMapper.selectByPrimaryKey(auditId);
        AuditDetail auditDetail = auditDetailMapper.selectByPrimaryKey(auditId);
        AssertUtil.isTrue(audit != null, "审核数据为空");
        AssertUtil.isTrue(auditDetail != null, "审核数据为空");
        AssertUtil.isTrue(audit.getStatus()==AuditStatusEnum.YES.getCode(), "数据已经被审核，不能重复提交");
        String txt = auditDetail.getAuditText();
        AuditDetailReq req = JSON.parseObject(txt, AuditDetailReq.class);
        AssertUtil.isTrue(audit.getVerificationCode().equals(regCode), "验证码错误！");
        String type = req.getType();
        Object parameter = req.getData();
        String methodName = req.getMethod();
        Method method = null;
        Object obj = null;
        if (type.equals(AuditTypeEnum.GROUP_TYPE.getCode())) {
            JobGroupService jobGroupService = context.getBean(JobGroupService.class);
            obj = jobGroupService;
            method = jobGroupService.getClass().getDeclaredMethod(methodName, XxlJobGroup.class);
            parameter=JSON.parseObject(JSON.toJSONString(parameter),XxlJobGroup.class);
        } else if (type.equals(AuditTypeEnum.TASK_TYPE.getCode())) {
            XxlJobService xxlJobService = context.getBean(XxlJobService.class);
            obj = xxlJobService;
            method = xxlJobService.getClass().getDeclaredMethod(methodName, XxlJobInfo.class);
            parameter=JSON.parseObject(JSON.toJSONString(parameter),XxlJobInfo.class);
        }

        if (method != null) {
            Object ret = execMethod(method, obj, parameter);
            auditMapper.updateAudioStatus(auditId, AuditStatusEnum.NO.getCode());
            return ret;
        }
        return false;

    }

    private Object execMethod(Method m, Object obj, Object parameter)  {
        try {
         Object ret=   m.invoke(obj, parameter);
          return ret;
        } catch (IllegalAccessException e) {
            throw new BizBaseException(438,e.getMessage());
        } catch (InvocationTargetException e) {
            throw new BizBaseException(438,e.getMessage());
        }
    }

}
