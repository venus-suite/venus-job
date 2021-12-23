package io.suite.venus.job.admin.common.exception;



import io.suite.venus.job.admin.common.inout.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;



@ControllerAdvice
@ResponseBody
public class ExceptionHandlerAdvice {
	
	@Value("${logging.config}")
	private String logbackFile;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 业务异常.
	 *
	 * @param e
	 *            the e
	 *
	 * @return the wrapper
	 */
	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public BaseResponse<?> businessException(ServiceException e) {
		String errorStackMsgs = getExceptionToString(e);
		logger.warn("业务异常：{}", errorStackMsgs);
		return BaseResponse.valueOfError(500,e.getMessage());
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public BaseResponse<?> bindException(BindException e) {
		String errorStackMsgs = getExceptionToString(e);
		logger.warn("业务异常：{}", errorStackMsgs);
		return BaseResponse.valueOfError(601, e.getMessage());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public BaseResponse<?> cnstraintViolationException(ConstraintViolationException e) {
		String errorStackMsgs = getExceptionToString(e);
		logger.warn("业务异常：{}", errorStackMsgs);
		return BaseResponse.valueOfError(601, e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public BaseResponse<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
		if (bindingResult != null) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			if (errors != null && !errors.isEmpty()) {
				return BaseResponse.valueOfError(601, errors.get(0).getDefaultMessage());
			}
		}
		String errorStackMsgs = getExceptionToString(e);
		logger.warn("参数格式错误：{}", errorStackMsgs);
		return BaseResponse.valueOfError(5501, "参数校验异常");
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public BaseResponse<?> httpMessageNotReadableException(HttpMessageNotReadableException e) {
		String errorStackMsgs = getExceptionToString(e);
		logger.warn("参数格式错误：{}", errorStackMsgs);
		return BaseResponse.valueOfError(5502, "参数校验异常");
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public BaseResponse<?> runtimeException(Exception e) {
		String errorStackMsgs = e.getMessage();
		logger.error("系统内部异常-{}", errorStackMsgs);
		return BaseResponse.valueOfError(5502,errorStackMsgs);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public BaseResponse<?> businessException(Exception e) {
		String errorStackMsgs = getExceptionToString(e);
		logger.error("系统内部异常-{}", errorStackMsgs);
		return BaseResponse.valueOfError(500,e.getMessage());
	}

	
	private String getExceptionToString(Exception e){
		if(logbackFile!=null&&logbackFile.contains("pro")){
			return ExceptionUtils.getExceptionToString(e,true);
		}
		return ExceptionUtils.getExceptionToString(e,false);
	}
}
