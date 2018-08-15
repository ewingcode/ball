package com.ewing.order.busi.res.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ewing.order.busi.res.dao.WebResourceScreenDao;
import com.ewing.order.busi.res.ddl.WebResourceScreenshot;
import com.ewing.order.cache.ResourceScreenshotCache;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 *
 * @author tanson lam
 * @creation 2017年2月6日
 * 
 */
@Component
public class WebResourceScreenshotService {
	@Resource
	private WebResourceScreenDao webResourceScreenDao;

	@Resource
	private ResourceScreenshotCache resourceScreenshotCache;
	/**
	 * 为每个资源获取第一张图片
	 * 
	 * @param resourceIds
	 * @return
	 */
	public Map<Integer, WebResourceScreenshot> getFirstForResources(List<Integer> resourceIds) {
		Map<Integer, WebResourceScreenshot> map = Maps.newHashMap();
		List<WebResourceScreenshot> list = webResourceScreenDao.getResScreenshot(resourceIds);
		if (CollectionUtils.isEmpty(list))
			return map;
		for (WebResourceScreenshot entity : list) {
			// 只选择第一位的图片
			if (entity.getRank() == 0)
				map.put(entity.getResourceId(), entity);
		}
		return map;
	}
 

	/**
	 * 为每个资源获取所有图片
	 * 
	 * @param resourceIds
	 * @return
	 * @throws Exception 
	 */
	public  List<String>  getAllForResource(Integer resourceId) throws Exception {
		List<WebResourceScreenshot> list = resourceScreenshotCache.getScreeByResourceId(resourceId);  
		final List<String> imageList = Lists.transform(list, new Function<WebResourceScreenshot, String>() {
			@Override
			public String apply(WebResourceScreenshot webResourceScreenshot) {
				return webResourceScreenshot.getImageUrl();
			}
		}); 
		for(String image : imageList){
			if(StringUtils.isEmpty(image))
				return Lists.newArrayList();
		}
		return imageList;
	}
 
}
