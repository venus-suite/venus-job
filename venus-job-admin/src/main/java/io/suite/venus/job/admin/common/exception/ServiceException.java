package io.suite.venus.job.admin.common.exception;

public class ServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5585582048976317791L;

	private IExceptionType iExceptionType;

	public ServiceException(IExceptionType iExceptionType) {
		super(iExceptionType.getMsg());
		this.iExceptionType = iExceptionType;

	}

	public IExceptionType getiExceptionType() {
		return iExceptionType;
	}

	public void setiExceptionType(IExceptionType iExceptionType) {
		this.iExceptionType = iExceptionType;
	}

}
