package com.ewing.order.busi.shop.dao;

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Component;

import com.ewing.order.busi.shop.ddl.ShopTable;
import com.ewing.order.core.jpa.BaseDao;

/**
 * 商家餐台DAO
 * 
 * @author tansonlam
 * @createDate 2016年3月1日
 * 
 */
@Component
public class ShopTableDao {
	@Resource
	public BaseDao baseDao;

	public ShopTable findOne(Integer shopId, Integer tableId) {
		Validate.notNull(shopId, "tableId不能为空");
		Validate.notNull(shopId, "shopId不能为空");
		return baseDao.findOne("shop_id=" + shopId + " and id=" + tableId, ShopTable.class);
	}

}
