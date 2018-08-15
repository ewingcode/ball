package com.ewing.order.util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.ewing.order.core.web.common.RestResult;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author tansonlam
 * @create 2016年12月12日
 * 
 */
public class RestClient {

	public static <T> RestResult<T> postJson(String token, String url,
			Object body, Class<T> responseClazz) {
		Map<String, String> headerAttribute = new HashMap<String, String>();
		headerAttribute.put("Content-Type", "application/json");
		headerAttribute.put("Accept", "application/json");
		headerAttribute.put("Authorization", "Bearer " + token);
		String response = HttpUtils.requestJson(url, "POST", null,
				headerAttribute);
		Type responseType = new TypeToken<RestResult<T>>() {
		}.getType();
		return JsonUtils.jsonToObject(response, responseType);
	}
}
