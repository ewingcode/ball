package com.ewing.order.core.oauth;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.ewing.order.common.prop.OAuthProp;
import com.ewing.order.core.web.common.RestResult;
import com.ewing.order.util.HttpUtils;
import com.ewing.order.util.JsonUtils;
import com.google.gson.reflect.TypeToken;

/**
 * OAuth的客户端管理
 * 
 * @author tansonlam
 * @create 2016年12月7日
 * 
 */
public class OAuthClientManage {
	/**
	 * 查询接入客户端信息接口地址
	 */
	private static String QUERY_CLIENT_DETAIL_URL;
	/**
	 * 查询JWT密钥的接口地址
	 */
	private static String QUERY_JWTSECRETKEY_URL;
	/**
	 * 接入客户端的缓存信息
	 */
	private final static Map<String, OauthClientDetailsDto> clientDetailCache = new ConcurrentHashMap<String, OauthClientDetailsDto>();
	/**
	 * Jwt的密钥缓存信息
	 */
	private final static Map<String, JwtSecretKey> jwtSecretCache = new ConcurrentHashMap<String, JwtSecretKey>();
	/**
	 * token的版本特征字符
	 */
	public final static String TOKEN_VERISON_SIGN = "_version=";

	private final static Type JSON_TYPE_JWTSECRET;
	private final static Type JSON_TYPE_CLIENTDETAIL;

	static {
		QUERY_CLIENT_DETAIL_URL = "query_client/";
		QUERY_JWTSECRETKEY_URL = "jwt/secretkey/";
		JSON_TYPE_JWTSECRET = new TypeToken<RestResult<JwtSecretKey>>() {
		}.getType();
		JSON_TYPE_CLIENTDETAIL = new TypeToken<RestResult<OauthClientDetailsDto>>() {
		}.getType();
	}

	private static JwtSecretKey queryJwtSecretKey(String version) {
		String reqUrl =  OAuthProp.oauthServer +QUERY_JWTSECRETKEY_URL + version;
		String response = HttpUtils.requestJson(reqUrl, "POST", null, null);
		if (response == null)
			return null;
		RestResult<JwtSecretKey> restResult = JsonUtils.jsonToObject(response, JSON_TYPE_JWTSECRET);
		if (restResult != null)
			return restResult.getData();
		return null;
	}

	/**
	 * 获取接入客户端的信息
	 * 
	 * @param clientId
	 * @return
	 */
	private static OauthClientDetailsDto queryClientDetail(String clientId) {
		String reqUrl = OAuthProp.oauthServer + QUERY_CLIENT_DETAIL_URL + clientId;
		String response = HttpUtils.requestJson(reqUrl, "POST", null, null);
		if (response == null)
			return null;
		RestResult<OauthClientDetailsDto> restResult = JsonUtils.jsonToObject(response, JSON_TYPE_CLIENTDETAIL);
		if (restResult != null)
			return restResult.getData();
		return null;
	}

	/**
	 * 获取当前匹配版本的密钥
	 * 
	 * @param version
	 * @return
	 * @throws OAuthException
	 */
	public static String getJwtSecretKey(String version) throws OAuthException {
		if (jwtSecretCache.containsKey(version))
			return jwtSecretCache.get(version).getSecretKey();

		JwtSecretKey jwtSecretKey = queryJwtSecretKey(version);
		if (jwtSecretKey == null || StringUtils.isEmpty(jwtSecretKey.getSecretKey())) {
			throw new OAuthException("can't found version[" + version + "]'s secret key.");
		}
		jwtSecretCache.put(version, jwtSecretKey);
		return jwtSecretKey.getSecretKey();
	}

	/**
	 * 获取接入客户端的密钥
	 * 
	 * @param clientId
	 * @return
	 * @throws OAuthException
	 */
	public static String getClientSecretKey(String clientId) throws OAuthException {
		if (clientDetailCache.containsKey(clientId))
			return clientDetailCache.get(clientId).getClientSecret();

		OauthClientDetailsDto oauthClientDetailsDto = queryClientDetail(clientId);
		if (oauthClientDetailsDto == null || StringUtils.isEmpty(oauthClientDetailsDto.getClientSecret())) {
			throw new OAuthException("can't found client[" + clientId + "]'s secret key.");
		}
		clientDetailCache.put(clientId, oauthClientDetailsDto);
		return oauthClientDetailsDto.getClientSecret();
	}

	public static void main(String[] args) {
		OauthClientDetailsDto oauthClientDetailsDto = queryClientDetail("unity-client");
		System.out.println(oauthClientDetailsDto);
		queryJwtSecretKey("999");
	}
}
