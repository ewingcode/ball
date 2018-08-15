package com.ewing.order.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class GsonUtil {

	private static String[] excluseClasses = { "BaseDO" };
	private static final ThreadLocal<Gson> LOCAL = new ThreadLocal<Gson>() {
		@Override
		protected Gson initialValue() {
			return new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
				@Override
				public boolean shouldSkipField(FieldAttributes f) {
					return ArrayUtils.contains(excluseClasses,
							f.getDeclaringClass().getSimpleName());
				}

				@Override
				public boolean shouldSkipClass(Class<?> clazz) {
					return false;
				}
			}).disableHtmlEscaping().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		}
	};

	public static Gson getGson() {
		return LOCAL.get();
	}

	private static Comparator<String> getComparator() {
		Comparator<String> c = new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		};

		return c;
	}

	public static String sort(String json) {
		JsonParser p = new JsonParser();
		JsonElement e = p.parse(json);
		sort(e);
		return getGson().toJson(e); 
	}

	public static void sort(JsonElement e) {
		if (e.isJsonNull()) {
			return;
		}

		if (e.isJsonPrimitive()) {
			return;
		}

		if (e.isJsonArray()) {
			JsonArray a = e.getAsJsonArray();
			for (Iterator<JsonElement> it = a.iterator(); it.hasNext();) {
				sort(it.next());
			}
			return;
		}

		if (e.isJsonObject()) {
			Map<String, JsonElement> tm = new TreeMap<String, JsonElement>(getComparator());
			for (Entry<String, JsonElement> en : e.getAsJsonObject().entrySet()) {
				tm.put(en.getKey(), en.getValue());
			}

			for (Entry<String, JsonElement> en : tm.entrySet()) {
				e.getAsJsonObject().remove(en.getKey());
				e.getAsJsonObject().add(en.getKey(), en.getValue());
				sort(en.getValue());
			}
			return;
		}
	}

	public static void main(String[] args) {
		String json = "{'user':'aa','pwd':'123'}";
		System.out.println(sort(json));
		System.out.println(SignManage.MD5("holle").toLowerCase()); 
	}
}
