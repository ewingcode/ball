package com.ewing.order.core.oauth;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ewing.order.util.JsonUtils;
import com.google.common.collect.Maps;

/**
 * @author tansonlam
 */
@Component
public class JwtUtil {
	private final static Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	@SuppressWarnings("unchecked")
	public static JwtClaim parseToken(String token) throws Exception {
		if (StringUtils.isEmpty(token))
			throw new OAuthException("token can not be null.");
		try {
			int versionIndex = token.lastIndexOf(OAuthClientManage.TOKEN_VERISON_SIGN);
			String version = token.substring(versionIndex).replace(OAuthClientManage.TOKEN_VERISON_SIGN, "");
			token = token.substring(0, versionIndex).replace("Bearer", "").replace(" ", "");

			Map<String, Object> clientInfo = getClientInfoFromToken(token, OAuthClientManage.getJwtSecretKey(version));
			if (clientInfo.isEmpty())
				throw new OAuthException("client info is empty.");
			String clientId = (String) clientInfo.get("client_id");
			// fetch client secret key
			String clientSecretKey = OAuthClientManage.getClientSecretKey(clientId);
			String userToken = (String) clientInfo.get("token");
			Collection<Map<String, String>> clientAuthorities = (Collection<Map<String, String>>) clientInfo
					.get("client_authority");
			if (CollectionUtils.isEmpty(clientAuthorities))
				throw new OAuthException("no auth to accessing application.");
			// 验证是否有访问本应用的权限
			/*
			 * boolean allowAccessApplication = false; for (Map<String, String>
			 * auth : clientAuthorities) { if
			 * (auth.containsValue(PorpertiesConfigurer
			 * .getProperty("application.name"))) { allowAccessApplication =
			 * true; break; } } if (!allowAccessApplication) throw new
			 * OAuthException("no auth to accessing application.");
			 */

			if (StringUtils.isEmpty(userToken))
				throw new OAuthException("user's token is empty.");

			JwtClaim jwtClaim = getClaimFromToken(userToken, clientSecretKey);

			if (StringUtils.isEmpty(jwtClaim.getUserName()))
				throw new OAuthException("no found user name.");

			return jwtClaim;
		} catch (OAuthException e) {
			logger.error(e.getMessage() + ",fail to decode token" + token);
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getClientInfoFromToken(String token, String secretKey) {
		if (StringUtils.isEmpty(token))
			return Maps.newConcurrentMap();
		SignatureVerifier verifier = new MacSigner(secretKey);
		Jwt jwt = JwtHelper.decodeAndVerify(token, verifier);
		return JsonUtils.jsonToObject(jwt.getClaims(), Map.class);
	}

	/**
	 * 将用户的token信息转换成JwtClaim对象
	 * 
	 * @param token
	 * @param secretKey
	 * 
	 * @return
	 */
	public static JwtClaim getClaimFromToken(String token, String secretKey) {
		if (StringUtils.isEmpty(token))
			return null;

		token = token.replace("Bearer", "").replace(" ", "");
		SignatureVerifier verifier = new MacSigner(secretKey);
		Jwt jwt = JwtHelper.decodeAndVerify(token, verifier);
		return JsonUtils.jsonToObject(jwt.getClaims(), JwtClaim.class);
	}

	public static void main(String[] args) {
		String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidW5pdHktcmVzb3VyY2UiXSwidXNlcl9uYW1lIjoidW5pdHkiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNDgxMTE5NTM0LCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiLCJST0xFX1VOSVRZIl0sImp0aSI6IjMzNmY2Yzg3LThjNTUtNGI0ZC04OTJkLWRiMWE5OTA4NzQ1ZiIsImNsaWVudF9pZCI6InVuaXR5LWNsaWVudCJ9.0wHZ6m8dppjw0HLGP4FVVgAAJzqsul_pRMtKYhm-8pk";
		System.out.println(getClaimFromToken(token, "123"));
	}
}
