package com.ewing.order.common.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OAuth参数
 * 
 * @author tanson lam
 * @creation 2016年12月14日
 * 
 */
@Component
@ConfigurationProperties(prefix = "oauth")
public class OAuthProp {

	public static String oauthServer; 
	
	public static String getOauthServer() {
		return oauthServer;
	}

	public static void setOauthServer(String oauthServer) {
		OAuthProp.oauthServer = oauthServer;
	}
	 

	 
}
