package io.suite.venus.job.admin.common.exception;



public enum ExceptionType implements IExceptionType {

	SYS_ERROR(500, "系统内部异常"),
	AUTH_ERROR(401, "权限不足"),
	TOKEN_TIMEOUT_ERROR(402, "登录已过期，请重新登陆"),
	UPDATE_USER_ID_NULL(406, "用户ID不能为空"),
	USER_DATA_NULL(407, "用户信息不存在"),
	USER_LOGIN_ERROR(408, "用户账号或密码输入有误，请重新输入"),
	;

	private ExceptionType(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	private int code;
	private String msg;
	private String showMsg;
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	public String getShowMsg() {
		return showMsg;
	}
	
	

}
