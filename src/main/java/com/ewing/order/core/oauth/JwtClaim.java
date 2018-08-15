package com.ewing.order.core.oauth;

import java.util.Arrays;

/**
 *
 * @author tanson lam
 * @creation 2016年12月7日
 * 
 */
public class JwtClaim {
	// "aud":["unity-resource"],"user_name":"unity","scope":["read","write"],"exp":1481119534,"authorities":["ROLE_USER","ROLE_UNITY"],"jti":"336f6c87-8c55-4b4d-892d-db1a9908745f","client_id":"unity-client"
	private String[] aud;
	private String user_name;
	private String[] scope;
	private long exp;
	private String[] authorities;
	private String jti;
	private String client_id;

	public String[] getAud() {
		return aud;
	}

	public void setAud(String[] aud) {
		this.aud = aud;
	}

	public String getUserName() {
		return user_name;
	}

	public void setUserName(String user_name) {
		this.user_name = user_name;
	}

	public String[] getScope() {
		return scope;
	}

	public void setScope(String[] scope) {
		this.scope = scope;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public String[] getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String[] authorities) {
		this.authorities = authorities;
	}

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	public String getClientId() {
		return client_id;
	}

	public void setClientId(String client_id) {
		this.client_id = client_id;
	}

	@Override
	public String toString() {
		return "JwtClaim [aud=" + Arrays.toString(aud) + ", user_name=" + user_name + ", scope="
				+ Arrays.toString(scope) + ", exp=" + exp + ", authorities=" + Arrays.toString(authorities) + ", jti="
				+ jti + ", client_id=" + client_id + "]";
	}

}
