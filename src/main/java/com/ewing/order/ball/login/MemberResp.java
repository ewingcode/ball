package com.ewing.order.ball.login;

import java.io.Serializable;

import com.ewing.order.util.GsonUtil;

/**
 * 会员信息
 *
 * <pre>
 * {"betterOdds":true,"passcode":"1236"}
 * </pre>
 * 
 * @author tansonlam
 * @create 2018年7月20日
 */
public class MemberResp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean betterOdds;
	private String passcode;

	public Boolean getBetterOdds() {
		return betterOdds;
	}

	public void setBetterOdds(Boolean betterOdds) {
		this.betterOdds = betterOdds;
	}

	public String getPasscode() {
		return passcode;
	}

	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}

	public static MemberResp fromResp(String jsonStr) {
		return (MemberResp) GsonUtil.getGson().fromJson(jsonStr, MemberResp.class);
	}
}
