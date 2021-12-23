package io.suite.venus.job.admin.common.log.logback;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

public class LogbackMsgInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5458053745808844450L;
	
	private String title;

	private String url;
	
	private String level;
	
	private String traceId;
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss.SSS")
	private Date time;
	
	private String thread;
	
	private String msg;
	
	private String userAgent;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getThread() {
		return thread;
	}

	public void setThread(String thread) {
		this.thread = thread;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getUrl() {
		return url;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	
	

}
