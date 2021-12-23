package io.suite.venus.job.admin.common.log.logback;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;


@Component
public class LogBackFilter implements Filter {


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		this.insertIntoMDC(request);
		try {
			chain.doFilter(request, response);
		} finally {
			this.clearMDC();
		}

	}

	@Override
	public void destroy() {

	}

	private void insertIntoMDC(ServletRequest request) {
		String traceId=UUID.randomUUID().toString().replace("-", "").toLowerCase();
		MDC.put("req.traceId", traceId);
		MDC.put("req.remoteHost", request.getRemoteHost());
		if (!(request instanceof HttpServletRequest)) {
		    return ;
		}
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		MDC.put("req.requestURI", httpServletRequest.getRequestURI());
		StringBuffer requestURL = httpServletRequest.getRequestURL();
		if (requestURL != null) {
			MDC.put("req.requestURL", requestURL.toString());
		}
		String requestNo = httpServletRequest.getHeader("Request-No");
		if (requestNo != null && !requestNo.equals("")) {
			MDC.put("req.requestNo", requestNo);
		}
		MDC.put("req.method", httpServletRequest.getMethod());
		MDC.put("req.queryString", httpServletRequest.getQueryString());
		MDC.put("req.userAgent", httpServletRequest.getHeader("User-Agent"));
		MDC.put("req.xForwardedFor", httpServletRequest.getHeader("X-Forwarded-For"));
		MDC.put("req.hostIp", getHostIp());
	}

	private void clearMDC() {
		MDC.remove("req.traceId");
		MDC.remove("req.remoteHost");
		MDC.remove("req.requestURI");
		MDC.remove("req.queryString");
		MDC.remove("req.requestURL");
		MDC.remove("req.method");
		MDC.remove("req.userAgent");
		MDC.remove("req.xForwardedFor");
		MDC.remove("req.requestNo");
		MDC.remove("req.hostIp");
	}


	public String getHostIp() {
		try {
			InetAddress address = InetAddress.getLocalHost();
			return address.getHostAddress();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
}
