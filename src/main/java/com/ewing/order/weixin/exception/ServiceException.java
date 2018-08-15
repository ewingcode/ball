package com.ewing.order.weixin.exception;

import com.ewing.order.weixin.dto.ErrorCode;

/**
 * 
 * @author liangjie
 *
 * @create 2017年2月7日
 */
public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7906950461806610914L;

	private String msg;

	private Throwable e;

	private ErrorCode code;

	public ServiceException(String msg, Throwable e) {
		super();
		this.msg = msg;
		this.e = e;
	}

	public ServiceException(Throwable e, ErrorCode code) {
		super();
		this.e = e;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Throwable getE() {
		return e;
	}

	public void setE(Throwable e) {
		this.e = e;
	}

	public ErrorCode getCode() {
		return code;
	}

	public void setCode(ErrorCode code) {
		this.code = code;
	}

}
