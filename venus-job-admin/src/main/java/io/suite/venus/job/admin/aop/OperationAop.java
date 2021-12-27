package io.suite.venus.job.admin.aop;

import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.common.util.AssertUtil;
import io.suite.venus.job.admin.controller.annotation.AuditLimit;
import io.suite.venus.job.admin.controller.audit.AuditContext;
import io.suite.venus.job.admin.controller.audit.AuditOpera;
import io.suite.venus.job.admin.core.model.Namespace;
import io.suite.venus.job.admin.core.model.UserNamespace;
import io.suite.venus.job.admin.core.model.XxlJobGroup;
import io.suite.venus.job.admin.core.model.XxlJobInfo;
import io.suite.venus.job.admin.domain.dto.AuditDetailReq;
import io.suite.venus.job.admin.domain.dto.UserInfo;
import io.suite.venus.job.admin.domain.dto.UserInfoContext;
import io.suite.venus.job.admin.enums.AuditTypeEnum;
import io.suite.venus.job.admin.enums.OperaCodeEnum;
import io.suite.venus.job.admin.service.JobGroupService;
import io.suite.venus.job.admin.service.NamespaceService;
import io.suite.venus.job.admin.service.RobotService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作审核通用方法
 */
@Aspect
@Component
public class OperationAop {

    @Value("${showDetailUrl}")
    private  String showDetailUrl;

    @Autowired
    private AuditOpera auditOpera;
    @Autowired
    private NamespaceService namespaceService;
    @Autowired
    private JobGroupService jobGroupService;

    @Autowired
    private RobotService robotService;


    @Pointcut("@annotation(io.suite.venus.job.admin.controller.annotation.AuditLimit)")
    public void opAop() {

    }

    @Around("opAop()")
    public Object audit(ProceedingJoinPoint pj) throws Throwable {

        Object[] args = pj.getArgs();
        MethodSignature methodSignature = (MethodSignature) pj.getSignature();
        Method m = methodSignature.getMethod();
        AuditLimit auditLimit = m.getDeclaredAnnotation(AuditLimit.class);
        if (auditLimit != null) {
            OperaCodeEnum operaCodeEnum = auditLimit.code();
            return processAudit(pj, m, args, operaCodeEnum);
        }
        return pj.proceed(args);
    }

    /**
     * 处理审核数据
     *
     * @param operaCodeEnum
     */
    private Object processAudit(ProceedingJoinPoint pj, Method m, Object[] args, OperaCodeEnum operaCodeEnum) throws Throwable {
        boolean needAudit = false;
        AuditContext context = new AuditContext();
        context.setCode(operaCodeEnum.getCode());

        AuditDetailReq detailReq = new AuditDetailReq();
        String webHook=null;
        if (args.length == 1) {
            Object o = args[0];
            long namespaceId = 0;
            Namespace namespace = null;
            if (o instanceof XxlJobGroup) {
                XxlJobGroup group = (XxlJobGroup) o;
                namespaceId = group.getNamespaceId();
                context.setTargetId(group.getId());
                detailReq.setType(AuditTypeEnum.GROUP_TYPE.getCode());
            } else if (o instanceof XxlJobInfo) {
                XxlJobInfo jobInfo = (XxlJobInfo) o;
                int jobGroupId = jobInfo.getJobGroup();
                context.setTargetId(jobInfo.getId());
                XxlJobGroup group = jobGroupService.getGroupById(jobGroupId);
                AssertUtil.isTrue(group != null, "获取执行器失败！");
                namespaceId = group.getNamespaceId();
                detailReq.setType(AuditTypeEnum.TASK_TYPE.getCode());
            }

            if (namespace == null) {
                namespace = namespaceService.getNamespaceById(namespaceId);
            }
            AssertUtil.isTrue(namespace != null, "获取命名空间失败！");
            UserInfo userInfo = UserInfoContext.getUserInfo();
            context.setUserInfo(userInfo);
            String email = userInfo.getEmail();

            UserNamespace userNamespace = namespaceService.getUserNamespaceByEmail(email, namespaceId);
            AssertUtil.isTrue(userNamespace != null, "没有权限操作");

            detailReq.setData(o);
            detailReq.setDesc(operaCodeEnum.getDesc());
            context.setDetail(detailReq);
            context.setNamespace(namespace);
            detailReq.setMethod(m.getName());

            if (!StringUtils.isEmpty(namespace.getAuditWebHook()) &&
                    namespace.getAuditWebHook().trim().startsWith("http")
            ) {
                needAudit = true;
            } else {
                needAudit = false;
            }
            webHook=namespace.getAuditWebHook();

        }


        if (needAudit) {
            BaseResponse<Boolean> response = auditOpera.addAuditData(context);
            if (response.getData()) {
                //发送审核通知消息
                String msg = buildMsg(context);
                String adminUser = context.getNamespace().getAdminUser();
                List<String> userList = null;
                if (!StringUtils.isEmpty(adminUser)) {
                    userList = Arrays.stream(adminUser.split(",")).collect(Collectors.toList());
                }
                if(!StringUtils.isEmpty(webHook)){
                    AssertUtil.isTrue(robotService.sendRobot(webHook,msg, userList), "发送验证码失败");
                }
            }
            BaseResponse ret= BaseResponse.valueOfSuccess("执行成功");
            ret.setMsg("此操作已经提交申请，请获取验证码到审核列表执行操作！");
            return  ret;
        } else {
             return pj.proceed(args);
        }

    }


    /**
     * 消息模板
     *
     * @param context
     * @return
     */
    private String buildMsg(AuditContext context) {
        AuditDetailReq detailReq = context.getDetail();
        String desc = detailReq.getDesc();
        StringBuffer sb = new StringBuffer();
        UserInfo userInfo = UserInfoContext.getUserInfo();
        sb.append(userInfo.getEmail() + " 提交了一个定时任务审核\n");
        sb.append("审核类型：" + desc + "\n");
        sb.append("验证码：" + context.getAudit().getVerificationCode() + "\n");
        long auditId = context.getAudit().getAuditId();
        String showUrl = String.format(showDetailUrl, auditId);
        sb.append("审核详情：" + showUrl );

        return sb.toString();
    }
}
