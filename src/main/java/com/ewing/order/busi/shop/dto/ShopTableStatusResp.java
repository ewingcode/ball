package com.ewing.order.busi.shop.dto;

import java.io.Serializable;

/**
 * 餐桌状态请求
 * 
 * @author tanson lam
 * @creation 2017年2月6日
 * 
 */
public class ShopTableStatusResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 餐桌ID
	 */
	private Integer tableId;
	/**
	 * 餐桌名称
	 */
	private String tableName;
	/**
	 * 状态 0:空闲 1:占用
	 */
	private String status;

	
	public ShopTableStatusResp() {

	}

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
