package com.ewing.order.busi.res.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.res.ddl.WebResource;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.core.jpa.BaseDao;
import com.ewing.order.core.jpa.util.PageBean; 

/**
 * 商品DAO
 *
 * @author tanson lam
 * @creation 2017年2月6日
 *
 */
@Component
public class WebResourceDao {

	@Resource
	public BaseDao baseDao;

	/**
	 * 查找单个资源信息
	 * 
	 * @param resourceId
	 * @return
	 */
	public WebResource findOne(Integer shopId, Integer resourceId) {
		return baseDao.findOne(
				"id  = " + resourceId + " and shop_id=" + shopId + " and iseff='" + IsEff.EFFECTIVE + "' ",
				WebResource.class);
	}

	public List<WebResource> queryByCategory(Integer shopId, Integer categoryId, Integer page, Integer pageSize) {
		StringBuilder sql = new StringBuilder("select wr.* from web_resource wr"
				+ " inner join web_category wc on wr.category_id = wc.id" + " where 1= 1");
		sql.append(" and wc.shop_id = ").append(shopId);
		sql.append(" and wc.shop_id = ").append(shopId);
		sql.append(" and wc.id = ").append(categoryId);
		sql.append(" and wc.iseff = '").append(IsEff.EFFECTIVE).append("'");
		sql.append(" and wr.iseff = '").append(IsEff.EFFECTIVE).append("'");
		sql.append(" order by wr.last_update desc");

		PageBean<WebResource> pageBean = baseDao.noMappedObjectPageQuery(sql.toString(), WebResource.class, page,
				pageSize);
		return pageBean.getResult();
	}
}
