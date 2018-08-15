package com.ewing.order.busi.res.dto;

import com.ewing.order.core.web.common.RestRequest;
/**
 * 分类下的产品信息请求
 *
 * @author tanson lam
 * @creation 2017年2月6日
 *
 */
public class CategoryProductInfoReq extends RestRequest {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 分类ID
	 */
	private Integer categoryId;
	/**
	 * 商铺ID
	 */
	private Integer shopId;

	public CategoryProductInfoReq() {

	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
}
