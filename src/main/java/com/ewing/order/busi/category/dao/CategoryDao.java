package com.ewing.order.busi.category.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.category.ddl.WebCategory;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.core.jpa.BaseDao;

/**
 *
 * @author tanson lam
 * @creation 2017年1月11日
 * 
 */
@Component
public class CategoryDao {
	@Resource
	private BaseDao baseDao;

	/**
	 * 查找用户的資源分类
	 * 
	 * @param shopId
	 * @param categoryId
	 * @return
	 */
	public WebCategory findOne(Integer shopId, Integer categoryId) {
		return baseDao.findOne("id=" + categoryId + " and shop_id=" + shopId, WebCategory.class);
	}

	public List<WebCategory> findAllCategory(Integer shopId) {
		return baseDao.find("shop_id=" + shopId + " and iseff='" + IsEff.EFFECTIVE + "' order by sort",
				WebCategory.class);
	}

}
