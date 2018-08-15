package com.ewing.order.common.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author 47652
 * @since 2017-02-08
 */
@Component
@ConfigurationProperties(prefix = "weixin")
public class WeixinProp {

	/** 微信支付分配的商户号 */
	public static String mchId;
	/** 微信appId **/
	public static String appId;
	/** 微信appId 对应的secret **/
	public static String appSecret;
	/** 微信服务器验证的token **/
	public static String token;
	/** 获取微信号 **/
	public static String mpId;
	/** 微信支付异步通知回调地址 */
	public static String payNotifyUrl;
	/** 微信支付 key **/
	public static String payKey;

	public static String getMchId() {
		return mchId;
	}

	public static void setMchId(String mchId) {
		WeixinProp.mchId = mchId;
	}

	public static String getAppId() {
		return appId;
	}

	public static void setAppId(String appId) {
		WeixinProp.appId = appId;
	}

	public static String getAppSecret() {
		return appSecret;
	}

	public static void setAppSecret(String appSecret) {
		WeixinProp.appSecret = appSecret;
	}

	public static String getToken() {
		return token;
	}

	public static void setToken(String token) {
		WeixinProp.token = token;
	}

	public static String getMpId() {
		return mpId;
	}

	public static void setMpId(String mpId) {
		WeixinProp.mpId = mpId;
	}

	public static String getPayNotifyUrl() {
		return payNotifyUrl;
	}

	public static void setPayNotifyUrl(String payNotifyUrl) {
		WeixinProp.payNotifyUrl = payNotifyUrl;
	}

	public static String getPayKey() {
		return payKey;
	}

	public static void setPayKey(String payKey) {
		WeixinProp.payKey = payKey;
	}

}
