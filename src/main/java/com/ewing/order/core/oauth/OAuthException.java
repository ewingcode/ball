package com.ewing.order.core.oauth;

/**
 * 
 * @author tansonlam
 * @create 2016��12��12��
 * 
 */
public class OAuthException extends Exception {

	private static final long serialVersionUID = 1L;

	public OAuthException(Exception e) {
		super(e);
	}

	public OAuthException(String msg) {
		super(msg);
	}

	public OAuthException(String msg, Throwable e) {
		super(msg, e);
	}
}
