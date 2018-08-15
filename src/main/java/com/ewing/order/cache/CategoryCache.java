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

import com.ewing.order.busi.category.ddl.WebCategory;
import com.ewing.order.core.cache.AbstractCache;
import com.ewing.order.core.cache.CacheModelType;
import com.ewing.order.core.cache.FetchCacheManager;
import com.ewing.order.core.cache.FetchCacheOperatorInterface;
import com.ewing.order.core.cache.PreparedCacheModel;
import com.ewing.order.core.redis.RedisException;
import com.ewing.order.util.SqlUtil;
import com.google.common.collect.Lists;

/**
 * 分类缓存
 * 
 * @author tanson lam
 * @creation 2017年2月8日
 * 
 */
@Component
public class CategoryCache extends AbstractCache<WebCategory> {

	private final static String CATEGORY_KEY = "CATEGORY_";

	private final static String CATEGEORY_INDEXLIST_KEY = "CATEGEORY_INDEXLIST_KEY";

	@Override
	public String prefixKey() {
		return CATEGORY_KEY;
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
	public List<WebCategory> updateCacheData(Long shopId, List<Long> ids) {
		List<WebCategory> resultList = queryEntitiyList(shopId, ids);
		if (CollectionUtils.isNotEmpty(resultList)) {
			updateCacheForEntityList(resultList);
			return resultList;
		}
		return Lists.newArrayList();
	}

	@Override
	public List<WebCategory> queryEntitiyList(Long shopId, List<Long> ids) {
		return baseModelService.find(
				"shop_id=" + shopId + " and id in (" + SqlUtil.array2InCondition(ids) + ")",
				WebCategory.class);

	}

	/**
	 * 获取商品信息
	 * 
	 * @param shopId
	 * @param resourceId
	 * @return
	 * @throws Exception
	 */
	public WebCategory getCategory(final Integer shopId, final Integer categoryId) {
		Validate.notNull(shopId, "shopId can not be null");
		Validate.notNull(categoryId, "categoryId can not be null");
		return FetchCacheManager.getData(this,
				new FetchCacheOperatorInterface<WebCategory, WebCategory>() {

					@Override
					public WebCategory refreshEntity() {
						return baseModelService.findOne(
								"shop_id=" + shopId + " and id =" + categoryId, WebCategory.class);
					}

					@Override
					public WebCategory getFromCache() throws RedisException {
						return getCache(localCacheKey());
					}

					@Override
					public String localCacheKey() {
						return buildCacheKey(CATEGORY_KEY, shopId, categoryId);
					}
				});
	}

	public List<WebCategory> getCategoryByShopId(final Integer shopId) {
		Validate.notNull(shopId, "shopId cant not be null");

		return FetchCacheManager.getData(this,
				new FetchCacheOperatorInterface<List<WebCategory>, List<WebCategory>>() {

					@Override
					public List<WebCategory> refreshEntity() {
						return baseModelService.find("shop_id=" + shopId + " order by sort",
								WebCategory.class);
					}

					@Override
					public List<WebCategory> getFromCache() throws RedisException {
						Map<String, WebCategory> dtoMap = getMapValueByIndex(localCacheKey(),
								CATEGORY_KEY);
						if (MapUtils.isEmpty(dtoMap))
							return null;
						return map2List(dtoMap);
					}

					@Override
					public String localCacheKey() {
						return buildCacheKey(CATEGEORY_INDEXLIST_KEY, shopId);
					}
				});

	}

	private List<WebCategory> map2List(Map<String, WebCategory> dtoMap) {
		if (dtoMap == null || dtoMap.isEmpty())
			return Lists.newArrayList();
		List<WebCategory> list = new ArrayList<WebCategory>();
		for (String id : dtoMap.keySet()) {
			list.add(dtoMap.get(id));
		}
		Collections.sort(list, new WebCategoryComparator());
		return list;
	}

	static public class WebCategoryComparator implements Comparator<WebCategory> {

		@Override
		public int compare(WebCategory o1, WebCategory o2) {
			if (o1.equals(o2) || o1.getSort().equals(o2.getSort()))
				return 0;

			if (o1.getSort() > o2.getSort())
				return 1;
			return -1;
		}
	}

	@Override
	protected List<PreparedCacheModel> wrap4CacheModelList(List<WebCategory> entity) {
		List<PreparedCacheModel> preparedCacheList = Lists.newArrayList();
		{
			Map<String, Object> map = new HashMap<String, Object>();
			for (WebCategory webCategory : entity) {
				map.put(buildCacheKey(CATEGORY_KEY, webCategory.getShopId(), webCategory.getId()),
						webCategory);
			}
			preparedCacheList.add(new PreparedCacheModel(CacheModelType.KEY_VALUE, map));
		}

		{
			Map<String, List<WebCategory>> webCategoryListMaps = entityList2GroupMap(entity,
					new GroupMapOperation<WebCategory>() {
						@Override
						public void dtoGroup(WebCategory entity,
								Map<String, List<WebCategory>> resultMap) {
							dto2WarehouseGroupMap(entity, resultMap);
						}
					});
			preparedCacheList
					.add(new PreparedCacheModel(CacheModelType.MAP_INDEX, webCategoryListMaps));
		}
		return preparedCacheList;
	}

	private void dto2WarehouseGroupMap(WebCategory dto,
			Map<String, List<WebCategory>> warehouseMap) {

		String key = buildCacheKey(CATEGEORY_INDEXLIST_KEY, dto.getShopId());
		if (StringUtils.isEmpty(key))
			return;

		List<WebCategory> list = warehouseMap.get(key);
		if (list == null) {
			list = Lists.newArrayList();
			warehouseMap.put(key, list);
		}
		list.add(dto);
	}

}
