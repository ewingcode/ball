package com.ewing.order.busi.shop.dto;

import java.io.Serializable;

/**
 * 餐桌状态请求
 * 
 * @author tanson lam
 * @creation 2017年2月6日
 * 
 */
public class ShopTableStatusReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 商铺ID
	 */
	private Integer shopId;
	/**
	 * 餐桌ID
	 */
	private Integer tableId;
	/**
	 * 客户ID
	 */
	private Integer customerId;

	public ShopTableStatusReq() {

	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

}
