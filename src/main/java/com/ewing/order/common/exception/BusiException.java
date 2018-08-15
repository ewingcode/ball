package com.ewing.order.common.exception;

/**
 * 业务异常
 * 
 * @author tansonlam
 * @createDate 2016年2月22日
 * 
 */
public class BusiException extends RuntimeException {
 
	private static final long serialVersionUID = 1L;

	public BusiException() {
		super();
	}

	public BusiException(String msg) {
		super(msg);
	}

	public BusiException(String message, Throwable cause) {
		super(message, cause);
	}
}
