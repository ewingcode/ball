package com.ewing.order.busi.weixin.dto;

public enum BusiType {

	ORDER("1", "订单"), REFUND("2", "退款");

	private String value;
	private String msg;

	BusiType(String value, String msg) {
		this.value = value;
		this.msg = msg;
	}

	public String getValue() {
		return value;
	}

	public String getMsg() {
		return msg;
	}

}
