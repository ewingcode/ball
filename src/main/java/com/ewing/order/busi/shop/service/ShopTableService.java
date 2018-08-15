package com.ewing.order.busi.shop.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.shop.dao.ShopTableDao;
import com.ewing.order.busi.shop.ddl.ShopTable;

/**
 *
 * @author tanson lam
 * @creation 2017年2月6日
 * 
 */
@Component
public class ShopTableService {
	@Resource
	private ShopTableDao shopTableDao;
	/**
	 * 获取商铺某个餐桌
	 * 
	 * @param shopId
	 * @param tableId
	 * @return
	 */
	public ShopTable findOne(Integer shopId, Integer tableId) {
		return shopTableDao.findOne(shopId, tableId);
	}
}
