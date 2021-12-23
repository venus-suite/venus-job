package io.suite.venus.job.admin.service;

import com.alibaba.fastjson.JSON;
import io.suite.venus.job.admin.common.inout.BaseRequest;
import io.suite.venus.job.admin.common.util.IpRequestUtil;
import io.suite.venus.job.admin.core.model.OperateLog;
import io.suite.venus.job.admin.domain.dto.UserInfo;
import io.suite.venus.job.admin.domain.dto.UserInfoContext;
import io.suite.venus.job.admin.mapper.OperateLogEntityMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class OperateLogService {

	@Autowired
	private OperateLogEntityMapper operateLogEntityMapper;



	public OperateLog getLog(List<Object> logParams, Object result, Long useTime,
								   OperateLog operateLogEntity) {
		if (operateLogEntity == null) {
			operateLogEntity = new OperateLog();
		}
		List<Object> newParamList = Lists.newArrayList();
		if(logParams!=null) {
			for (Object object : logParams) {
				if (object.getClass().isPrimitive() || (object instanceof BaseRequest)) {
					newParamList.add(object);
				}
			}
		}
		try {
			operateLogEntity.setParams(JSON.toJSONString(logParams));
		}catch (Exception ex){}

		operateLogEntity.setUseTime(useTime.intValue());
		operateLogEntity.setCreateTime(new Date());
		HttpServletRequest httpServletRequest = this.getHttpServletRequest();
		if (httpServletRequest != null) {
			UserInfo userInfoContext = UserInfoContext.getUserInfo();
			if (userInfoContext != null) {
				operateLogEntity.setUserEmail(userInfoContext.getEmail());
				operateLogEntity.setUsername(String.valueOf(userInfoContext.getUserName()));
			}
			String ip = IpRequestUtil.getIpAddr(httpServletRequest);
			operateLogEntity.setIp(ip);
			Map<String, String> headers = this.getHeadersInfo(httpServletRequest);
			operateLogEntity.setHeaders(JSON.toJSONString(headers));
			String url = httpServletRequest.getRequestURL().toString();
			operateLogEntity.setUrl(url);
		}
		return operateLogEntity;
	}

	@Async
	public void addOperatedLog(OperateLog operateLogEntity) {
		operateLogEntityMapper.insert(operateLogEntity);
	}

	private HttpServletRequest getHttpServletRequest() {
		ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes());
		if (servletRequestAttributes != null) {
			HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
			return httpServletRequest;
		}
		return null;
	}

	private Map<String, String> getHeadersInfo(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<?> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}



}
