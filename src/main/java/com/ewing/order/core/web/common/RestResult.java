package com.ewing.order.core.web.common;

import java.io.Serializable;

import com.ewing.order.core.web.base.ResponseCode;

/**
 * 
 *
 * @author tanson lam
 * @creation 2017年1月5日
 *
 */
public class RestResult<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private String code;
	private String message;
	private Boolean success;
	private T data;
 

	public static RestResult<String> successResult() {
		RestResult<String> r = new RestResult<String>();
		r.setData("success");
		r.setSuccess(true);
		r.setCode(String.valueOf(ResponseCode.OK));
		r.setMessage("success");
		return r;
	}

	public static <T> RestResult<T> successResult(T result) {
		RestResult<T> r = new RestResult<T>();
		r.setData(result);
		r.setSuccess(true);
		r.setCode(String.valueOf(ResponseCode.OK));
		r.setMessage("success");
		return r;
	}

	public static <T> RestResult<T> errorResult(String errorMessage) {
		RestResult<T> r = new RestResult<T>();
		r.setCode(String.valueOf(ResponseCode.UNKOWN_ERROR));
		r.setSuccess(false);
		r.setMessage(errorMessage);
		return r;
	}

	public static <T> RestResult<T> errorResult(int errCode, String errorMessage) {
		RestResult<T> r = new RestResult<T>();
		r.setCode(String.valueOf(errCode));
		r.setSuccess(false);
		r.setMessage(errorMessage);
		return r;
	}

	public static <T> RestResult<T> errorResult(String errorCode,
			String errorMessage) {
		RestResult<T> r = new RestResult<T>();
		r.setCode(errorCode);
		r.setSuccess(false);
		r.setMessage(errorMessage);
		return r;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	} 

}
