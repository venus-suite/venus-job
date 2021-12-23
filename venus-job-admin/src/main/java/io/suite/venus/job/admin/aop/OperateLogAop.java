package io.suite.venus.job.admin.aop;


import com.alibaba.fastjson.JSON;
import io.suite.venus.job.admin.common.log.ActionOperateLog;
import io.suite.venus.job.admin.common.log.OperateThreadLoacalMap;
import io.suite.venus.job.admin.core.model.OperateLog;
import io.suite.venus.job.admin.core.util.JsonUtils;
import io.suite.venus.job.admin.service.OperateLogService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.lang.reflect.Method;
import java.util.List;

@Aspect
@Component
@Order(2)
@Slf4j
public class OperateLogAop {

	@Autowired
	private OperateLogService operateLogService;

	@Pointcut("@annotation(io.suite.venus.job.admin.common.log.ActionOperateLog)")
	public void logAop() {
	}

	/**
	 * 异常统一处理
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around(value = "logAop()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		Method method = this.getMethod(pjp);
		OperateLog operateLogEntityNew = new OperateLog();
		ActionOperateLog operateLog = method.getAnnotation(ActionOperateLog.class);
		Object[] params = pjp.getArgs();
		long startTime = System.currentTimeMillis();
		if (operateLog != null) {

			List<Object> logParams = Lists.newArrayList();
			for (Object object : params) {
				if (object instanceof HttpServletResponseWrapper) {
					continue;
				}
				if (!(object instanceof HttpServletRequest)) {
					logParams.add(object);
				} else if (!(object instanceof HttpServletResponse)) {
					logParams.add(object);
				}
			}

			long endTime = System.currentTimeMillis();
			long useTime = endTime - startTime;

			operateLogEntityNew = this.operateLogService.getLog(logParams, result, useTime,
					operateLogEntityNew);
			operateLogEntityNew.setEventName(operateLog.eventName());
			OperateThreadLoacalMap.putMsg(operateLogEntityNew);
		}

		result = pjp.proceed(params);
		if (operateLog != null && result != null) {
			operateLogEntityNew.setResponseBody(JSON.toJSONString(result));
			this.operateLogService.addOperatedLog(operateLogEntityNew);
			log.info("管理后台操作日志添加成功");
		}

		return result;
	}



	@AfterThrowing(throwing = "ex", pointcut = "logAop()")

	public void doException( Exception ex) {
		OperateLog operateLogEntity = OperateThreadLoacalMap.getMsg();
		if(operateLogEntity!=null){
			if(ex!=null && ex.getMessage()!=null){
				operateLogEntity.setResponseBody(JsonUtils.toJSONString(ex.getMessage()));
				this.operateLogService.addOperatedLog(operateLogEntity);
				log.info("管理后台操作日志添加失败");
			}
		}

	}


	public Method getMethod(JoinPoint pjp) {
		Method method = null;
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		method = signature.getMethod();
		return method;
	}

}
