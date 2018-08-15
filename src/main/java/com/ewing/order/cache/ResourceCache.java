package com.ewing.order.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Component;

import com.ewing.order.busi.res.ddl.WebResource;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.core.cache.AbstractCache;
import com.ewing.order.core.cache.CacheModelType;
import com.ewing.order.core.cache.FetchCacheManager;
import com.ewing.order.core.cache.FetchCacheOperatorInterface;
import com.ewing.order.core.cache.PreparedCacheModel;
import com.ewing.order.core.jpa.util.PageBean;
import com.ewing.order.core.redis.RedisException;
import com.ewing.order.util.SqlUtil;
import com.google.common.collect.Lists;

/**
 *
 * @author tanson lam
 * @creation 2017年2月9日
 * 
 */
@Component
public class ResourceCache extends AbstractCache<WebResource> {
	private final static String RESOURCE_KEY = "RESOURCE_";

	private final static String RESOURCE_FORSHOP_INDEX_KEY = "RESOURCE_FORSHOP_INDEX_KEY";
	private final static String RESOURCE_CATEGORY_SORTLIST_KEY = "RESOURCE_CATEGORY_SORTLIST_KEY";

	@Override
	public String prefixKey() {
		return RESOURCE_KEY;
	}

	@Override
	public Boolean useLocalCache() {
		return false;
	}

	@Override
	public void initialCache() throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<WebResource> updateCacheData(Long shopId, List<Long> ids) {
		List<WebResource> resultList = queryEntitiyList(shopId, ids);
		if (CollectionUtils.isNotEmpty(resultList)) {
			updateCacheForEntityList(resultList);
			return resultList;
		}
		return Lists.newArrayList();
	}

	@Override
	public List<WebResource> queryEntitiyList(Long shopId, List<Long> ids) {
		return baseModelService.find(
				"shop_id=" + shopId + " and id in (" + SqlUtil.array2InCondition(ids) + ")",
				WebResource.class);
	}

	@Override
	protected List<PreparedCacheModel> wrap4CacheModelList(List<WebResource> entity) {
		List<PreparedCacheModel> preparedCacheList = Lists.newArrayList();
		{
			Map<String, Object> map = new HashMap<String, Object>();
			for (WebResource webResource : entity) {
				map.put(buildCacheKey(RESOURCE_KEY, webResource.getShopId(), webResource.getId()),
						webResource);
			}
			preparedCacheList.add(new PreparedCacheModel(CacheModelType.KEY_VALUE, map));
		}

		/*
		 * { Map<String, Map<WebResource, Double>> webCategoryListMaps =
		 * entityList2SortSet(entity, new SortSetOperation<WebResource>() {
		 * 
		 * @Override public void dtoGroup(WebResource entity, Map<String,
		 * Map<WebResource, Double>> resultMap) { dto2CategorySortList(entity,
		 * resultMap); } }); preparedCacheList.add(new
		 * PreparedCacheModel(CacheModelType.SORTSET, webCategoryListMaps,
		 * SORTLIST_EXPIRESECOND)); }
		 */

		{
			Map<String, List<WebResource>> shopMaps = entityList2GroupMap(entity,
					new GroupMapOperation<WebResource>() {
						@Override
						public void dtoGroup(WebResource entity,
								Map<String, List<WebResource>> resultMap) {
							dto2ShopMap(entity, resultMap);
						}
					});
			preparedCacheList.add(new PreparedCacheModel(CacheModelType.MAP_INDEX, shopMaps));
		}
		return preparedCacheList;
	}

	private void dto2ShopMap(WebResource dto, Map<String, List<WebResource>> shopMaps) {

		String key = buildCacheKey(RESOURCE_FORSHOP_INDEX_KEY, dto.getShopId());
		if (StringUtils.isEmpty(key))
			return;

		List<WebResource> list = shopMaps.get(key);
		if (list == null) {
			list = Lists.newArrayList();
			shopMaps.put(key, list);
		}
		list.add(dto);
	}

	/*
	 * private void dto2CategorySortList(WebResource dto, Map<String,
	 * Map<WebResource, Double>> categoryListMaps) { String key =
	 * buildCacheKey(RESOURCE_CATEGORY_SORTLIST_KEY, dto.getShopId(),
	 * dto.getCategoryId()); if (StringUtils.isEmpty(key)) return;
	 * 
	 * Map<WebResource, Double> map = categoryListMaps.get(key); if (map ==
	 * null) { map = Maps.newConcurrentMap(); categoryListMaps.put(key, map); }
	 * map.put(dto, Double.valueOf(dto.getId())); }
	 */

	/**
	 * 根据分类获取商品信息
	 * 
	 * @param shopId
	 * @param categoryId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<WebResource> getResourceByCategory(final Integer shopId, final Integer categoryId,
			final Integer page, final Integer pageSize) {
		Validate.notNull(shopId, "shopId can not be null");
		Validate.notNull(categoryId, "categoryId can not be null");
		Validate.notNull(page, "page can not be null");
		Validate.notNull(pageSize, "pageSize can not be null");
		return FetchCacheManager.getData(this,
				new FetchCacheOperatorInterface<List<WebResource>, List<WebResource>>() {

					@Override
					public List<WebResource> refreshEntity() {
						String condition = "shop_id=" + shopId + " and category_id=" + categoryId;
						PageBean<WebResource> pageResult = baseModelService.pageQuery(condition,
								null, page, pageSize, WebResource.class);
						return pageResult.getResult();
					}

					@Override
					public List<WebResource> getFromCache() throws RedisException {
						Map<String, WebResource> dtoMap = getSortListByIndex(localCacheKey(),
								RESOURCE_KEY, page, pageSize);
						if (MapUtils.isEmpty(dtoMap))
							return null;
						return map2List(dtoMap);
					}

					@Override
					public String localCacheKey() {
						return buildCacheKey(RESOURCE_CATEGORY_SORTLIST_KEY, shopId, categoryId);
					}
				});
	}

	/**
	 * 根据分类获取商品信息
	 * 
	 * @param shopId
	 * @param categoryId
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public List<WebResource> getResourceByShopId(final Integer shopId) {
		Validate.notNull(shopId, "shopId can not be null");
		return FetchCacheManager.getData(this,
				new FetchCacheOperatorInterface<List<WebResource>, List<WebResource>>() {

					@Override
					public List<WebResource> refreshEntity() {
						String condition = "shop_id=" + shopId + " and iseff='" + IsEff.EFFECTIVE
								+ "' and is_online='1'";
						return baseModelService.find(condition, WebResource.class);
					}

					@Override
					public List<WebResource> getFromCache() throws RedisException {
						Map<String, WebResource> dtoMap = getMapValueByIndex(localCacheKey(),
								RESOURCE_KEY);
						if (MapUtils.isEmpty(dtoMap))
							return null;
						return map2List(dtoMap);
					}

					@Override
					public String localCacheKey() {
						return buildCacheKey(RESOURCE_FORSHOP_INDEX_KEY, shopId);
					}
				});
	}

	/**
	 * 获取商品信息
	 * 
	 * @param shopId
	 * @param resourceId
	 * @return
	 * @throws Exception
	 */
	public WebResource getResource(final Integer shopId, final Integer resourceId) {
		Validate.notNull(shopId, "shopId can not be null");
		Validate.notNull(resourceId, "resourceId can not be null");
		return FetchCacheManager.getData(this,
				new FetchCacheOperatorInterface<WebResource, WebResource>() {

					@Override
					public WebResource refreshEntity() {
						return baseModelService.findOne(
								"shop_id=" + shopId + " and id =" + resourceId, WebResource.class);
					}

					@Override
					public WebResource getFromCache() throws RedisException {
						return getCache(localCacheKey());
					}

					@Override
					public String localCacheKey() {
						return buildCacheKey(RESOURCE_KEY, shopId, resourceId);
					}
				});
	}

	private List<WebResource> map2List(Map<String, WebResource> dtoMap) {
		if (dtoMap == null || dtoMap.isEmpty())
			return Lists.newArrayList();
		List<WebResource> list = new ArrayList<WebResource>();
		for (String id : dtoMap.keySet()) {
			list.add(dtoMap.get(id));
		}
		Collections.sort(list, new WebResourceComparator());
		return list;
	}

	static public class WebResourceComparator implements Comparator<WebResource> {

		@Override
		public int compare(WebResource o1, WebResource o2) {
			if (o1.equals(o2) || o1.getId().equals(o2.getId()))
				return 0;

			if (o1.getId() > o2.getId())
				return 1;
			return -1;
		}
	}
}
