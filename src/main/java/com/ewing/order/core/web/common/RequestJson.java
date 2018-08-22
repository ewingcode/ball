package com.ewing.order.core.web.common;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ewing.order.util.GsonUtil;

/**
 * 请求的json
 * 
 * @author tansonlam
 * @create 2016年12月28日
 * 
 */
public class RequestJson {
	/**
	 * 签名，根据请求数据和加密方式计算的签名值
	 */
	private String sign;
	/**
	 * 业务ID
	 */
	private String id;
	/**
	 * 请求的业务方
	 */
	private String clientId;
	/**
	 * 加密方式 MD5
	 */
	private String encrypt;
	/**
	 * 请求的数据
	 */
	private Map<String, Object> data;

	private String dataStr;

	public RequestJson() {

	}

	public RequestJson(String json) {
		RequestJson requestJson = GsonUtil.getGson().fromJson(json,
				RequestJson.class);
		String dataJson = GsonUtil.getGson().toJson(requestJson.getData());
		this.dataStr = dataJson;
		this.clientId = requestJson.getClientId();
		this.encrypt = requestJson.encrypt;
		this.data = requestJson.data;
		this.id = requestJson.id;
		this.sign = requestJson.sign;
	}

	public String getDataStr() {
		return dataStr;
	}

	public Integer getInteger(String key) {
		if (StringUtils.isEmpty(key))
			return null;
		if (data.get(key) instanceof String && data.get(key) != null)
			return Integer.valueOf(data.get(key).toString());
		return null;
		
	}

	public String getString(String key) {
		if (StringUtils.isEmpty(key))
			return null;
		return (String) data.get(key);
	}

	public Long getLong(String key) {
		if (StringUtils.isEmpty(key))
			return null;
		if (data.get(key) instanceof String && data.get(key) != null)
			return Long.valueOf(data.get(key).toString());
		return null;
	}

	public Object getObject(String key) {
		if (StringUtils.isEmpty(key))
			return null;
		return data.get(key);
	}

	public Float getFloat(String key) {
		if (StringUtils.isEmpty(key))
			return null; 
		if (data.get(key) instanceof String && data.get(key) != null)
			return Float.valueOf(data.get(key).toString());
		return null;
	}

	public <T> T requestJson2Obj(Class<T> clazz) {
		return GsonUtil.getGson().fromJson(dataStr, clazz);
	}

	public static RequestJson newEmpty() {
		return new RequestJson();
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	/*
	 * public String getData() { return data; }
	 * 
	 * public void setData(String data) { this.data = data; }
	 */

}
