package com.ewing.order.busi.res.dto;

/**
 * 
 * 产品详情请求参数
 * 
 * @author tansonlam
 * @createDate 2016年1月26日
 *
 */
public class ProductDetailReq {
	/**
	 * 商铺ID
	 */
	private Integer shopId;
	/**
	 * 产品ID
	 */
	private Integer resourceId;

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

}
