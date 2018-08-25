package com.ewing.order.common.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author 47652
 * @since 2017-02-08
 */
@Component
@ConfigurationProperties(prefix = "ballmatch")
public class BallmatchProp {

	/** 收集球赛账号 */
	public static String account;
	/** 收集球赛账号密码 **/
	public static String pwd;
	/** 皇冠网URL **/
	public static String url;
	/** 是否允许收集球赛数据*/
	public static Boolean allowcollect;

	public static Boolean getAllowcollect() {
		return allowcollect;
	}

	public static void setAllowcollect(Boolean allowcollect) {
		BallmatchProp.allowcollect = allowcollect;
	}

	public static void setAccount(String account) {
		BallmatchProp.account = account;
	}

	public static void setPwd(String pwd) {
		BallmatchProp.pwd = pwd;
	}

	public static void setUrl(String url) {
		BallmatchProp.url = url;
	}

	public static String getAccount() {
		return account;
	}

	public static String getPwd() {
		return pwd;
	}

	public static String getUrl() {
		return url;
	}

}
