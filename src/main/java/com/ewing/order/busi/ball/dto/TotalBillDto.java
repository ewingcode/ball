package com.ewing.order.busi.ball.dto;

import javax.persistence.Column;
 
public class TotalBillDto {
	@Column(name = "account")
	private String account;
	@Column(name = "matchCount")
	private Integer matchCount;
	@Column(name = "totalWin")
	private Float totalWin;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Integer getMatchCount() {
		return matchCount;
	}
	public void setMatchCount(Integer matchCount) {
		this.matchCount = matchCount;
	}
	public Float getTotalWin() {
		return totalWin;
	}
	public void setTotalWin(Float totalWin) {
		this.totalWin = totalWin;
	}
	 
	
	
}
