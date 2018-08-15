package com.ewing.order.busi.res.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ewing.order.busi.category.ddl.WebCategory;
import com.ewing.order.busi.category.service.CategoryService;
import com.ewing.order.busi.res.dao.WebResourceDao;
import com.ewing.order.busi.res.ddl.WebResource;
import com.ewing.order.busi.res.dto.CategoryProductInfoReq;
import com.ewing.order.busi.res.dto.CategoryProductInfoResp;
import com.ewing.order.busi.res.dto.LightProductInfo;
import com.ewing.order.busi.res.dto.LightProductListResp;
import com.ewing.order.busi.res.dto.ProductDetailDto;
import com.ewing.order.busi.res.dto.ProductDetailResp;
import com.ewing.order.cache.ResourceCache;
import com.google.common.collect.Lists;

/**
 * 商品服务
 * 
 * @author tanson lam
 * @creation 2017年2月6日
 * 
 */
@Component
public class WebResourceService {
	private final static Logger logger = LoggerFactory.getLogger(WebResourceService.class);
	@Resource
	private WebResourceDao webResourceDao;
	@Resource
	private WebResourceScreenshotService webResourceScreenshotService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private ResourceCache resourceCache;

	/**
	 * 根据categoryId查找某个商店的
	 * 
	 * @param categoryId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<LightProductListResp> queryByShopId(Integer shopId) throws Exception {
		Validate.notNull(shopId, "shopId不能为空");
		long start  = System.currentTimeMillis();
		List<WebResource> resourceList = resourceCache.getResourceByShopId(shopId);
		List<WebCategory> categoryList = categoryService.getCategoryByShopId(shopId);
		List<LightProductListResp> respList = Lists.newArrayList();
		for (WebCategory webCategory : categoryList) {
			LightProductListResp lightProductListResp = new LightProductListResp();
			lightProductListResp.setCategoryId(webCategory.getId());
			lightProductListResp.setCategoryName(webCategory.getName()); 
			for (WebResource webResource : resourceList) {
				if (webResource.getCategoryId().equals(webCategory.getId())) {
					LightProductInfo lightProductInfo = new LightProductInfo();
					BeanUtils.copyProperties(lightProductInfo, webResource);
					lightProductListResp.addProduct(lightProductInfo);
				}
			}
			respList.add(lightProductListResp);
		}
		System.out.println("cost:"+(System.currentTimeMillis()-start));
		return respList;
	}

	/**
	 * 根据categoryId查找某个商店的
	 * 
	 * @param categoryId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<CategoryProductInfoResp> queryByCategory(CategoryProductInfoReq req)
			throws Exception {
		Integer shopId = req.getShopId();
		Integer categoryId = req.getCategoryId();
		Integer page = req.getPage();
		Integer pageSize = req.getPageSize();
		Validate.notNull(categoryId, "categoryId不能为空");
		Validate.notNull(shopId, "shopId不能为空");
		Validate.notNull(page, "page不能为空");
		Validate.notNull(pageSize, "pageSize不能为空");

		List<CategoryProductInfoResp> dtoList = new ArrayList<CategoryProductInfoResp>();
		Map<Integer, WebCategory> categoryMap = categoryService.findAllCategory(shopId);
		List<WebResource> list = resourceCache.getResourceByCategory(shopId, categoryId, page,
				pageSize);

		for (WebResource entity : list) {
			CategoryProductInfoResp lightProductInfo = new CategoryProductInfoResp();
			try {
				BeanUtils.copyProperties(lightProductInfo, entity);
				lightProductInfo.setCategoryName(categoryMap.get(entity.getCategoryId()).getName());
				dtoList.add(lightProductInfo);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		return dtoList;
	}

	/**
	 * 查找单个资源信息
	 * 
	 * @param shopId
	 * @param resourceId
	 * @return
	 * @throws Exception
	 */
	public ProductDetailResp getProductDetail(Integer shopId, Integer resourceId) throws Exception {
		WebResource webresource = resourceCache.getResource(shopId, resourceId);
		if (webresource == null)
			return null;

		List<String> images = webResourceScreenshotService.getAllForResource(webresource.getId());
		WebCategory webCategory = categoryService.findOne(shopId, webresource.getCategoryId());
		ProductDetailResp detailResponse = new ProductDetailResp();
		ProductDetailDto productDetail = new ProductDetailDto();
		// 获取属性列表
		try {
			BeanUtils.copyProperties(productDetail, webresource);
			if (CollectionUtils.isNotEmpty(images))
				productDetail.setImages(images);
			if (webCategory != null)
				productDetail.setCategoryName(webCategory.getName());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		detailResponse.setProductDetail(productDetail);
		return detailResponse;
	}
}
