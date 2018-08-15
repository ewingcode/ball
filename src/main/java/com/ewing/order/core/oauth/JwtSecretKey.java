package com.ewing.order.core.oauth;

import java.io.Serializable;

/**
 * 
 * @author tanson lam
 * @creation 2016年12月12日
 * 
 */
public class JwtSecretKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 版本
	 */
	private String version;
	/**
	 * Jwt的密钥
	 */
	private String secretKey;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

}
