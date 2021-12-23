package io.suite.venus.job.admin.controller.interceptor;

import com.alibaba.fastjson.JSON;
import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.domain.dto.UserInfo;
import io.suite.venus.job.admin.domain.dto.UserInfoContext;
import io.suite.venus.job.admin.service.UserService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截
 *
 *
 */
@Component
public class PermissionInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	UserService userService;

	private   boolean checkUserLogin(HttpServletRequest request, HttpServletResponse response) throws  Exception {
		BaseResponse baseResponse = BaseResponse.valueOfError(401, "请先登录！");
		response.setCharacterEncoding("utf-8");
		setResponseHeader(response);
		String uid = request.getHeader("token");

		if (StringUtils.isEmpty(uid)) {
			uid = request.getHeader("token");
		}
		if (StringUtils.isEmpty(uid)) {
			response.getWriter().write(JSON.toJSONString(baseResponse));
			return false;
		}
		UserInfo userInfo = userService.getUserInfoById(uid, false);
		if (userInfo == null) {
			response.getWriter().write(JSON.toJSONString(baseResponse));
			return false;
		}
		UserInfoContext.setUserInfo(userInfo);
		return true;

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		if (!(handler instanceof HandlerMethod)) {
			return super.preHandle(request, response, handler);
		}

		String uri = request.getRequestURI();
		// 不做拦截
		if (uri.equals("/error") || uri.equals("/login")) {
			return super.preHandle(request, response, handler);
		}

		boolean isOK = checkUserLogin(request, response);
		if (isOK) {
			return super.preHandle(request, response, handler);
		} else {
			return false;
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		UserInfoContext.clear();
		PageHelper.clearPage();
		super.afterCompletion(request, response, handler, ex);
	}

	public void setResponseHeader(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
	}
}
