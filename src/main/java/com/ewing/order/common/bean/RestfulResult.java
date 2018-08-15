package com.ewing.order.common.bean;

/**
 * Restful调用结果公共类
 * 
 * @author tanson lam
 * @create 2016年9月6日
 */
public class RestfulResult {
	/**
	 * 返回编码 0为正常
	 */
	private Integer resultCode = 0;
	/**
	 * 接口返回提示
	 */
	private String resultMsg;

	/**
	 * 返回错误结果
	 * 
	 * @param resultCode
	 * @param resultMsg
	 * @return
	 */
	public void failResult(Integer resultCode, String resultMsg) {
		setResultCode(resultCode);
		setResultMsg(resultMsg);
	}

	public Integer getResultCode() {
		return resultCode;
	}

	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

}
