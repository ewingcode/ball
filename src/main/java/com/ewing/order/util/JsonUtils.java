package com.ewing.order.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * 
 * @author tanson lam
 * @creation 2016年8月15日
 * 
 */
public class JsonUtils {

	public static final Logger logger = LoggerFactory
			.getLogger(JsonUtils.class);

	public static final Gson gson = new GsonBuilder().setPrettyPrinting()
			.create();

	/**
	 * 判断json合法性
	 * 
	 * @param json
	 * @return 是/否
	 */
	public static boolean isGoodJson(String jsonStr) {
		if (StringUtils.isBlank(jsonStr)) {
			return false;
		}

		try {
			JsonElement jsonElement = new JsonParser().parse(jsonStr);
			return jsonElement.isJsonObject();
		} catch (Exception e) {
			logger.warn(jsonStr + " is invalid json str", e);
		}
		return false;
	}

	/**
	 * 判断json合法性
	 * 
	 * @param json
	 * @return 是/否
	 */
	private static boolean isGoodJson(String jsonStr, JsonElement jsonElement) {
		if (StringUtils.isBlank(jsonStr)) {
			return false;
		}

		try {
			return jsonElement.isJsonObject();
		} catch (Exception e) {
			logger.warn(jsonStr + " is invalid json str", e);
		}
		return false;
	}

	/**
	 * 取得json指定字段数据
	 * 
	 * @param jsonStr
	 * @param field
	 * @return
	 */
	public static String getDataInJson(String jsonStr, String field) {
		if (StringUtils.isEmpty(field)) {
			return StringUtils.EMPTY;
		}
		try {
			JsonElement jsonElement = new JsonParser().parse(jsonStr);
			if (isGoodJson(jsonStr, jsonElement)) {
				return jsonElement.getAsJsonObject().getAsJsonObject(field)
						.toString();
			}
		} catch (Exception e) {
			logger.warn(jsonStr + " is invalid json str", e);
		}
		return StringUtils.EMPTY;
	}

	/**
	 * 取得json指定字段数据
	 * 
	 * @param jsonStr
	 * @param field
	 * @return
	 */
	public static String getSMDataInJson(String jsonStr, String field) {
		if (StringUtils.isEmpty(field)) {
			return StringUtils.EMPTY;
		}
		try {
			JsonElement jsonElement = new JsonParser().parse(jsonStr);
			if (isGoodJson(jsonStr, jsonElement)) {
				return jsonElement.getAsJsonObject().getAsJsonArray(field)
						.toString();
			}
		} catch (Exception e) {
			logger.warn(jsonStr + " is invalid json str", e);
		}
		return StringUtils.EMPTY;
	}

	public static boolean isValidJsonObject(String jsonStr) {
		try {
			JsonElement jsonElement = new JsonParser().parse(jsonStr);
			if (!isGoodJson(jsonStr, jsonElement)) {
				return false;
			}

			JsonObject dataJsonObject = jsonElement.getAsJsonObject()
					.getAsJsonObject("data");
			if (dataJsonObject.isJsonNull()) {
				return false;
			} else if (dataJsonObject.has("list")) {
				if (dataJsonObject.getAsJsonArray("list").size() == 0)
					return false;
			}

		} catch (Exception e) {
			logger.warn(jsonStr + " is invalid json str", e);
			return false;
		}
		return true;
	}

	public static boolean isValidJsonArray(String jsonStr) {
		try {
			JsonElement jsonElement = new JsonParser().parse(jsonStr);
			if (!isGoodJson(jsonStr, jsonElement)) {
				return false;
			}

			if (jsonElement.getAsJsonObject().getAsJsonArray("items")
					.isJsonNull()) {
				return false;
			}
		} catch (Exception e) {
			logger.warn(jsonStr + " is invalid json str", e);
			return false;
		}
		return true;
	}

	/**
	 * 判断json合法性
	 * 
	 * @param json
	 * @return 是/否
	 */
	private static boolean isGoodJson(JsonElement jsonElement) {
		try {
			return jsonElement.isJsonObject();
		} catch (Exception e) {
			logger.warn(jsonElement.toString() + " is invalid json str", e);
		}
		return false;
	}

	/**
	 * 营销接口 取得json指定字段数据
	 * 
	 * @param jsonStr
	 * @param field
	 * @return
	 */
	public static String getDataInJson(String jsonStr) {
		if (StringUtils.isBlank(jsonStr)) {
			return StringUtils.EMPTY;
		}

		try {
			JsonElement jsonElement = new JsonParser().parse(jsonStr);
			if (isGoodJson(jsonElement)) {
				JsonObject dataJson = jsonElement.getAsJsonObject()
						.getAsJsonObject("data");
				if (dataJson.isJsonNull()) {
					return StringUtils.EMPTY;
				}
				return dataJson.toString();
			}
		} catch (Exception e) {
			logger.warn(jsonStr + " is invalid json str", e);
		}
		return StringUtils.EMPTY;
	}

	public static boolean isValidPPServerJsonData(String jsonStr) {
		try {
			JsonElement jsonElement = new JsonParser().parse(jsonStr);
			if (!isGoodJson(jsonElement)) {
				return false;
			}

			JsonObject dataJson = jsonElement.getAsJsonObject()
					.getAsJsonObject("data");
			if (dataJson.isJsonNull()) {
				return false;
			}

			if (!dataJson.has("content")) {
				return false;
			}

			JsonArray jsonArray = dataJson.getAsJsonArray("content");
			if (jsonArray.isJsonNull() || jsonArray.size() == 0) {
				return false;
			}
		} catch (Exception e) {
			logger.warn(jsonStr + " is invalid json str", e);
			return false;
		}
		return true;
	}

	public static String toJson(Object t) {
		if (null == t) {
			return StringUtils.EMPTY;
		}

		Gson gson = new Gson();
		return gson.toJson(t);
	}

	/**
	 * 把一个json字符串转换为指定类型对象
	 * 
	 * @param jsonStr
	 * @param type
	 * @return 目标对象 或 null
	 */
	public static <T> T jsonToObject(String jsonStr, Type type) {
		if (StringUtils.isEmpty(jsonStr) || type == null) {
			return null;
		}
		T t = null;
		try {
			t = gson.fromJson(jsonStr, type);
		} catch (Exception e) {
			logger.error("jsonToObject fail :  " + jsonStr, e);
		}
		return t;
	}

	/**
	 * 把一个json字符串转换为指定类型对象
	 * 
	 * @param jsonStr
	 * @param className
	 * @return
	 */
	public static Object jsonToObject(String jsonStr, String className) {
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
			return jsonToObject(jsonStr, clazz);
		} catch (ClassNotFoundException e) {
			logger.error("jsonToObject fail :  " + jsonStr, e);
			return null;
		}

	}

	/**
	 * 把一个json字符串转换为指定类型对象
	 * 
	 * @param jsonStr
	 * @param clazz
	 * @return 目标对象 或 null
	 */
	public static <T> T jsonToObject(String jsonStr, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonStr) || clazz == null) {
			return null;
		}
		T t = null;
		try {
			t = gson.fromJson(jsonStr, clazz);
		} catch (Exception e) {
			logger.error("jsonToObject fail : ", e);

		}
		return t;
	}
	
	  public static <U,T> U jsonToObject(String json, Class<U> clazz1, Class<T> clazz2) {
	        Gson gson = new Gson();
	        Type objectType = type(clazz1, clazz2);
	        return gson.fromJson(json, objectType);
	    }
	  
	   static <U> ParameterizedType type(final Class<U> raw, final Type... args) {
	        return new ParameterizedType() {
	            public Type getRawType() {
	                return raw;
	            }

	            public Type[] getActualTypeArguments() {
	                return args;
	            }

	            public Type getOwnerType() {
	                return null;
	            }
	        };
	    }
	
}
