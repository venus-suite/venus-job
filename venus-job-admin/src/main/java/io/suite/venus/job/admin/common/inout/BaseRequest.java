package io.suite.venus.job.admin.common.inout;

import java.io.Serializable;

public class BaseRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4995847684029868253L;

	
	private Long clientTime;
	
	
	private String sign;


	public Long getClientTime() {
		return clientTime;
	}


	public void setClientTime(Long clientTime) {
		this.clientTime = clientTime;
	}


	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}
	
	


}
