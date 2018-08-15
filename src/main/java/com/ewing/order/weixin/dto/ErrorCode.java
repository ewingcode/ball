package com.ewing.order.weixin.dto;

/**
 * 
 * @author liangjie
 *
 * @create 2017年2月7日
 */

public enum ErrorCode {

	SYSTEM_ERROR(100, "系统错误");

	int code;
	String msg;

	private ErrorCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
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
	
	

}
