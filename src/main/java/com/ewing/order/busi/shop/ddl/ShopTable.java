package com.ewing.order.busi.shop.ddl;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shop_table")
public class ShopTable implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Integer id;
	/**
	 * 商铺ID
	 */
	private Integer shopId;
	/**
	 * 区域ID
	 */ 
	private Integer tableAreaId;
	/**
	 * 餐桌名称
	 */
	private String tableName;
	/**
	 * 状态 0:空闲 1:占用
	 */
	private String status;
	/**
	 * 订单ID
	 */
	private Integer orderId;
	private String iseff;
	private Date createTime;
	private Date lastUpdate;

	public ShopTable() {
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getTableAreaId() {
		return tableAreaId;
	}

	public void setTableAreaId(Integer tableAreaId) {
		this.tableAreaId = tableAreaId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getIseff() {
		return iseff;
	}

	public void setIseff(String iseff) {
		this.iseff = iseff;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
