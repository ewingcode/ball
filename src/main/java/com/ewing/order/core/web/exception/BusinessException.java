package com.ewing.order.core.web.exception;

import com.ewing.order.core.web.base.ResponseCode;

/**
 * 业务错误异常。
 * 
 * @author tansonlam
 * @create 2016年12月29日
 * 
 */
public class BusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;

	public BusinessException() {
		super();
		setErrorCode(ResponseCode.UNKOWN_ERROR);
	}

	public BusinessException(Throwable cause) {
		super(cause);
		setErrorCode(ResponseCode.UNKOWN_ERROR);
	}

	public BusinessException(Throwable cause, int errorCode) {
		super(cause);
		setErrorCode(errorCode);
	}

	public BusinessException(Throwable cause, int errorCode, String message) {
		super(message, cause);
		setErrorCode(errorCode);
	}

	public BusinessException(int errorCode, String message) {
		super(message);
		setErrorCode(errorCode);
	}

	public BusinessException(int errorCode, String message, Object... args) {
		super(String.format(message, args));
		setErrorCode(errorCode);
	}

	public int getErrorCode() {
		return code;
	}

	public void setErrorCode(int errorCode) {
		this.code = errorCode;
	}

}