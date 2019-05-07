package com.ewing.order.busi.ball.dto;

import java.sql.Timestamp;
import java.util.List;

import com.ewing.order.busi.ball.ddl.BwContinue;

public class BetAutoBuyDto {
 

	public BetAutoBuyDto() {
		super();
	}

	private Integer id;
	private String account;
	private String is_login;
	private String iseff;
	private String money;
	private String phone;
	private String isTest;
	private String continueMaxMatch;
	private String continueStartLostnum; 
	private String stopWingold; 
	private String stopLosegold;
	private String continuePlanMoney;
	private Timestamp createTime;
	private Timestamp lastUpdate;
	private String ruleName;
	private List<String> ruleNameList;
	private TotalBillDto totalBillDto;
	private BwContinue bwContinue; 
	
	public BwContinue getBwContinue() {
		return bwContinue;
	}

	public void setBwContinue(BwContinue bwContinue) {
		this.bwContinue = bwContinue;
	}

	public List<String> getRuleNameList() {
		return ruleNameList;
	}

	public void setRuleNameList(List<String> ruleNameList) {
		this.ruleNameList = ruleNameList;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
 

	public TotalBillDto getTotalBillDto() {
		return totalBillDto;
	}

	public void setTotalBillDto(TotalBillDto totalBillDto) {
		this.totalBillDto = totalBillDto;
	}

	public String getContinuePlanMoney() {
		return continuePlanMoney;
	}

	public void setContinuePlanMoney(String continuePlanMoney) {
		this.continuePlanMoney = continuePlanMoney;
	}

	public String getStopWingold() {
		return stopWingold;
	}

	public void setStopWingold(String stopWingold) {
		this.stopWingold = stopWingold;
	}

	public String getStopLosegold() {
		return stopLosegold;
	}

	public void setStopLosegold(String stopLosegold) {
		this.stopLosegold = stopLosegold;
	}

	public String getContinueStartLostnum() {
		return continueStartLostnum;
	}

	public void setContinueStartLostnum(String continueStartLostnum) {
		this.continueStartLostnum = continueStartLostnum;
	}

	public String getIsTest() {
		return isTest;
	}

	public void setIsTest(String isTest) {
		this.isTest = isTest;
	}

	public String getContinueMaxMatch() {
		return continueMaxMatch;
	}

	public void setContinueMaxMatch(String continueMaxMatch) {
		this.continueMaxMatch = continueMaxMatch;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getIs_login() {
		return is_login;
	}

	public void setIs_login(String is_login) {
		this.is_login = is_login;
	}

	public String getIseff() {
		return iseff;
	}

	public void setIseff(String iseff) {
		this.iseff = iseff;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
