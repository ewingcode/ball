package com.ewing.order.busi.category.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.category.dao.CategoryDao;
import com.ewing.order.busi.category.ddl.WebCategory;
import com.ewing.order.busi.category.dto.CategoryTreeDto;
import com.ewing.order.cache.CategoryCache;

/**
 * 资源分类服务类
 * 
 * @author tanson lam
 * @creation 2015年5月13日
 */
@Component
public class CategoryService {

	@Resource
	private CategoryDao categoryDao;
	@Resource
	private CategoryCache categoryCache;

	/**
	 * 查找用户的資源分类
	 * 
	 * @param shopId
	 * @param categoryId
	 * @return
	 */
	public WebCategory findOne(Integer shopId, Integer categoryId) {
		return categoryCache.getCategory(shopId, categoryId);
	}

	
	public List<WebCategory> getCategoryByShopId(Integer shopId){
		return categoryCache.getCategoryByShopId(shopId);
	}
	
	/**
	 * 查询分类树结构
	 * 
	 * @param shopId
	 * @throws Exception
	 */
	public List<CategoryTreeDto> queryCatagoryTree(Integer shopId) {
		List<WebCategory> categoryList = categoryCache.getCategoryByShopId(shopId);
		List<CategoryTreeDto> moduleList = new ArrayList<CategoryTreeDto>();
		for (WebCategory category : categoryList) {
			CategoryTreeDto dto = new CategoryTreeDto(category.getId(), category.getParentid(),
					category.getName());
			moduleList.add(dto);
		}
		return moduleList;
	}

	/**
	 * 查找所有的分类
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Integer, WebCategory> findAllCategory(Integer shopId) {
		List<WebCategory> list = categoryCache.getCategoryByShopId(shopId);
		Map<Integer, WebCategory> map = new HashMap<Integer, WebCategory>();
		for (WebCategory webCategory : list) {
			map.put(webCategory.getId(), webCategory);
		}
		return map;
	}
}
