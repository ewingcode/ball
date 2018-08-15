package com.ewing.order.busi.ball.dto;

import com.ewing.order.ball.bk.bet.BkPreOrderViewResp;

public class BkBetRequest {
	private String account;
	private String uid;
	private String gid;
	private String gtype;
	private String golds;
	private String wtype;
	private String side;
	private String ioratio;
	private String con;
	private String ratio;
	private String timestamp2;

	public String getIoratio() {
		return ioratio;
	}

	public void setIoratio(String ioratio) {
		this.ioratio = ioratio;
	}

	public String getCon() {
		return con;
	}

	public void setCon(String con) {
		this.con = con;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getTimestamp2() {
		return timestamp2;
	}

	public void setTimestamp2(String timestamp2) {
		this.timestamp2 = timestamp2;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getGtype() {
		return gtype;
	}

	public void setGtype(String gtype) {
		this.gtype = gtype;
	}

	public String getGolds() {
		return golds;
	}

	public void setGolds(String golds) {
		this.golds = golds;
	}

	public String getWtype() {
		return wtype;
	}

	public void setWtype(String wtype) {
		this.wtype = wtype;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

}
