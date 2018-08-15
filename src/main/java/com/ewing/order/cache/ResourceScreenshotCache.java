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

import com.ewing.order.busi.res.ddl.WebResourceScreenshot;
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
public class ResourceScreenshotCache extends AbstractCache<WebResourceScreenshot> {

	private final static String RESSCREENSHOT_KEY = "RESSCREENSHOT_";

	private final static String RESSCREENSHOT_RES_INDEXLIST_KEY = "RESSCREENSHOT_RES_INDEXLIST_KEY";

	@Override
	public Boolean useLocalCache() {
		return false;
	}

	@Override
	public String prefixKey() {
		return RESSCREENSHOT_KEY;
	}

	@Override
	public void initialCache() throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<WebResourceScreenshot> updateCacheData(Long shopId, List<Long> ids) {
		List<WebResourceScreenshot> resultList = queryEntitiyList(shopId, ids);
		if (CollectionUtils.isNotEmpty(resultList)) {
			updateCacheForEntityList(resultList);
			return resultList;
		}
		return Lists.newArrayList();
	}

	@Override
	public List<WebResourceScreenshot> queryEntitiyList(Long shopId, List<Long> ids) {
		return baseModelService.find(" id in (" + SqlUtil.array2InCondition(ids) + ")",
				WebResourceScreenshot.class);

	}

	/**
	 * 获取指定商品的图片
	 * 
	 * @param resourceId
	 * @return
	 * @throws Exception
	 */
	public List<WebResourceScreenshot> getScreeByResourceId(final Integer resourceId)
			throws Exception {
		Validate.notNull(resourceId, "resourceId cant not be null");

		return FetchCacheManager.getData(this,
				new FetchCacheOperatorInterface<List<WebResourceScreenshot>, List<WebResourceScreenshot>>() {

					@Override
					public List<WebResourceScreenshot> refreshEntity() {
						List<WebResourceScreenshot> list = baseModelService.find(
								"resource_id=" + resourceId + " order by rank",
								WebResourceScreenshot.class);
						// 查询为空则设置一个空的对象
						if (CollectionUtils.isEmpty(list)) {
							list = Lists.newArrayList();
							WebResourceScreenshot webResourceScreenshot = new WebResourceScreenshot();
							webResourceScreenshot.setId(-1);
							webResourceScreenshot.setResourceId(resourceId);
							list.add(webResourceScreenshot);
						}
						return list;
					}

					@Override
					public List<WebResourceScreenshot> getFromCache() throws RedisException {
						Map<String, WebResourceScreenshot> dtoMap = getMapValueByIndex(
								localCacheKey(), RESSCREENSHOT_KEY);
						if (MapUtils.isEmpty(dtoMap))
							return null;
						return map2List(dtoMap);
					}

					@Override
					public String localCacheKey() {
						return buildCacheKey(RESSCREENSHOT_RES_INDEXLIST_KEY, resourceId);
					}
				});

	}

	private List<WebResourceScreenshot> map2List(Map<String, WebResourceScreenshot> dtoMap) {
		if (dtoMap == null || dtoMap.isEmpty())
			return Lists.newArrayList();
		List<WebResourceScreenshot> list = new ArrayList<WebResourceScreenshot>();
		for (String id : dtoMap.keySet()) {
			list.add(dtoMap.get(id));
		}
		Collections.sort(list, new WebResourceScreenshotComparator());
		return list;
	}

	static public class WebResourceScreenshotComparator
			implements Comparator<WebResourceScreenshot> {

		@Override
		public int compare(WebResourceScreenshot o1, WebResourceScreenshot o2) {
			if (o1.equals(o2) || o1.getRank() == o2.getRank())
				return 0;

			if (o1.getRank() > o2.getRank())
				return 1;
			return -1;
		}
	}

	@Override
	protected List<PreparedCacheModel> wrap4CacheModelList(List<WebResourceScreenshot> entity) {
		List<PreparedCacheModel> preparedCacheList = Lists.newArrayList();
		{
			Map<String, Object> map = new HashMap<String, Object>();
			for (WebResourceScreenshot dto : entity) {
				map.put(buildCacheKey(RESSCREENSHOT_KEY, dto.getId()), dto);
			}
			preparedCacheList.add(new PreparedCacheModel(CacheModelType.KEY_VALUE, map));
		}

		{
			Map<String, List<WebResourceScreenshot>> resourceListMaps = entityList2GroupMap(entity,
					new GroupMapOperation<WebResourceScreenshot>() {
						@Override
						public void dtoGroup(WebResourceScreenshot entity,
								Map<String, List<WebResourceScreenshot>> resultMap) {
							dto2ResourceGroupMap(entity, resultMap);
						}
					});
			preparedCacheList
					.add(new PreparedCacheModel(CacheModelType.MAP_INDEX, resourceListMaps));
		}

		return preparedCacheList;
	}

	private void dto2ResourceGroupMap(WebResourceScreenshot dto,
			Map<String, List<WebResourceScreenshot>> resourceMap) {

		String key = buildCacheKey(RESSCREENSHOT_RES_INDEXLIST_KEY, dto.getResourceId());
		if (StringUtils.isEmpty(key))
			return;

		List<WebResourceScreenshot> list = resourceMap.get(key);
		if (list == null) {
			list = Lists.newArrayList();
			resourceMap.put(key, list);
		}
		list.add(dto);
	}

}
