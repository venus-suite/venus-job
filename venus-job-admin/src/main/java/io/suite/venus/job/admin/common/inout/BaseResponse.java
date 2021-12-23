package io.suite.venus.job.admin.common.inout;

import io.suite.venus.job.admin.common.exception.IExceptionType;
import io.suite.venus.job.admin.common.exception.ServiceException;

public class BaseResponse<T> {

	// 错误码，200表示成功
	private int code = 200;
	// 错误信息
	private String msg = "操作成功";
	// 服务器时间
	private long timestamp;
	// 数据
	private T data;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


	public static <T> BaseResponse<T> valueOfSuccess(T data) {
		BaseResponse<T> baseResponse = new BaseResponse<T>();
		baseResponse.setData(data);
		baseResponse.setTimestamp(System.currentTimeMillis());
		return baseResponse;
	}

	public static <T> BaseResponse<T> valueOfSuccess() {
		BaseResponse<T> baseResponse = new BaseResponse<T>();
		baseResponse.setTimestamp(System.currentTimeMillis());
		return baseResponse;
	}

	public static <T> BaseResponse<T> valueOfError(ServiceException e) {
		IExceptionType ie = e.getiExceptionType();
		return valueOfError(ie);
	}

	public static <T> BaseResponse<T> valueOfError(IExceptionType e) {
		String msg = e.getMsg();
		if (e.getShowMsg() != null) {
			msg = e.getShowMsg();
		}
		return valueOfError(e.getCode(), msg);
	}

	public static <T> BaseResponse<T> valueOfSysError() {
		return valueOfError(500, "系统内部异常");
	}

	public static <T> BaseResponse<T> valueOfError(int code, String msg) {
		BaseResponse<T> baseResponse = new BaseResponse<T>();
		baseResponse.setTimestamp(System.currentTimeMillis());
		baseResponse.setCode(code);
		baseResponse.setMsg(msg);
		return baseResponse;
	}

}
