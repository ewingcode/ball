package com.ewing.order.core.cache;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 缓存对象更新的相关信息
 * 
 * @author tanson lam
 * @create 2016年11月12日
 * 
 */
public class CacheEntityUpdateInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	/**
	 * 实体变化通知事件
	 */
	private EntityNoticeEvent entityNoticeEvent;
	/**
	 * 实体对象ID
	 */
	private Long entityId;
	/**
	 * 商铺ID
	 */
	private Long shopId;
	/**
	 * 批次
	 */
	private String batchId;
	/**
	 * 实体关联的缓存KEY
	 */
	private List<CacheEntityKey> cacheKeys;
	/**
	 * 接受时间
	 */
	private Long receiveTime;

	public CacheEntityUpdateInfo(Long entityId, Long shopId,
			String batchId, List<CacheEntityKey> cacheKeys) {
		super();
		this.batchId = batchId;
		this.entityId = entityId;
		this.shopId = shopId;
		this.cacheKeys = cacheKeys; 
	}
 
	public void setEntityNoticeEvent(EntityNoticeEvent entityNoticeEvent) {
		this.entityNoticeEvent = entityNoticeEvent;
	}

	public EntityNoticeEvent getEntityNoticeEvent() {
		return entityNoticeEvent;
	}

	public Boolean isTimeOut(long timeOut) {
		return System.currentTimeMillis() - receiveTime >= timeOut;
	}

	public void setReceiveTime(Long receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	} 

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public List<CacheEntityKey> getCacheKeys() {
		return cacheKeys;
	}

	public void setCacheKeys(List<CacheEntityKey> cacheKeys) {
		this.cacheKeys = cacheKeys;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getPrimaryKey() {
		return shopId + "_" + entityId + "_" + batchId;
	}

}
