/**
 * 
 */
package com.ewing.order.busi.weixin.dto;

/**
 * @author 47652
 *
 * @since 2017年2月15日
 */
public class WxPayUnifiedOrdersDTO {

	/**
	 * 商品简单描述
	 */
	String msg;
	/**
	 * 商品详情
	 */
	String detail;
	/**
	 * 流水号
	 */
	String bizId;
	/**
	 * 金额，单位分
	 */
	Integer cost;

	/**
	 * 客户端IP
	 */
	String clientIp;
	/**
	 * 
	 */
	String goodsTag;
	/**
	 * 商品编码
	 */
	Integer resourceId;
	/**
	 * 用户在公众唯一标识
	 */
	String openId;

	String userId;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getGoodsTag() {
		return goodsTag;
	}

	public void setGoodsTag(String goodsTag) {
		this.goodsTag = goodsTag;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
