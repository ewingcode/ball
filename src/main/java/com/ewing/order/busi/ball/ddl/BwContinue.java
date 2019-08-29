package com.ewing.order.busi.ball.ddl;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 投注批次表
 * 
 * @author tanson lam
 * @create 2016年9月6日
 */
@Entity
@Table(name = "bw_continue")
public class BwContinue implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BwContinue() {
		super();
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
	@Column(name = "account")
	private String account; 
	@Column(name = "bet_detail")
	private String betDetail;
	@Column(name = "bet_rule_id")
	private Integer betRuleId;
	@Column(name = "target_money")
	private Float targetMoney;
	@Column(name = "total_bet_money")
	private Float totalBetMoney; 
	@Column(name = "continue_max_match")
	private Integer continueMaxMatch;
	@Column(name = "continue_start_lostnum")
	private Integer continueStartLostnum;
	@Column(name = "continue_plan_money")
	private String continuePlanMoney; 
	@Column(name = "win_gold")
	private Float winGold;
	@Column(name = "total_match")
	private Integer totalMatch;
	@Column(name = "allow_bet")
	private Integer allowBet;
	@Column(name = "status")
	private String status;
	@Column(name = "createTime")
	private Timestamp createTime;
	@Column(name = "lastUpdate")
	private Timestamp lastUpdate;
	//固定比例
	@Column(name = "pool_rate")
	private Float poolRate;
	//固定比例下注资金额池
	@Column(name = "rate_pool_money")
	private Float ratePoolMoney;
	//固定比例最新下注资金额池
	@Column(name = "rate_cur_pool_money")
	private Float rateCurPoolMoney;
	//固定比例下注赢多少钱则停止下注
	@Column(name = "rate_stop_wingold")
	private Float rateStopWingold;
	//固定比例下注输多少钱则停止下注
	@Column(name = "rate_stop_losegold")
	private Float rateStopLosegold; 
	
	
	public Float getRateCurPoolMoney() {
		return rateCurPoolMoney;
	}

	public void setRateCurPoolMoney(Float rateCurPoolMoney) {
		this.rateCurPoolMoney = rateCurPoolMoney;
	}

	public Float getPoolRate() {
		return poolRate;
	}

	public void setPoolRate(Float poolRate) {
		this.poolRate = poolRate;
	}

	public Float getRatePoolMoney() {
		return ratePoolMoney;
	}

	public void setRatePoolMoney(Float ratePoolMoney) {
		this.ratePoolMoney = ratePoolMoney;
	}

	public Float getRateStopWingold() {
		return rateStopWingold;
	}

	public void setRateStopWingold(Float rateStopWingold) {
		this.rateStopWingold = rateStopWingold;
	}

	public Float getRateStopLosegold() {
		return rateStopLosegold;
	}

	public void setRateStopLosegold(Float rateStopLosegold) {
		this.rateStopLosegold = rateStopLosegold;
	} 

	public String getContinuePlanMoney() {
		return continuePlanMoney;
	}

	public void setContinuePlanMoney(String continuePlanMoney) {
		this.continuePlanMoney = continuePlanMoney;
	}

	public Float getWinGold() {
		return winGold;
	}

	public void setWinGold(Float winGold) {
		this.winGold = winGold;
	}

	public Integer getContinueStartLostnum() {
		return continueStartLostnum;
	}

	public void setContinueStartLostnum(Integer continueStartLostnum) {
		this.continueStartLostnum = continueStartLostnum;
	}

	public String getBetDetail() {
		return betDetail;
	}

	public void setBetDetail(String betDetail) {
		this.betDetail = betDetail;
	}

	public Integer getContinueMaxMatch() {
		return continueMaxMatch;
	}

	public void setContinueMaxMatch(Integer continueMaxMatch) {
		this.continueMaxMatch = continueMaxMatch;
	}

	public Integer getTotalMatch() {
		return totalMatch;
	}

	public void setTotalMatch(Integer totalMatch) {
		this.totalMatch = totalMatch;
	}

	public Integer getAllowBet() {
		return allowBet;
	}

	public void setAllowBet(Integer allowBet) {
		this.allowBet = allowBet;
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

	public Integer getBetRuleId() {
		return betRuleId;
	}

	public void setBetRuleId(Integer betRuleId) {
		this.betRuleId = betRuleId;
	}

	public Float getTargetMoney() {
		return targetMoney;
	}

	public void setTargetMoney(Float targetMoney) {
		this.targetMoney = targetMoney;
	}

	public Float getTotalBetMoney() {
		return totalBetMoney;
	}

	public void setTotalBetMoney(Float totalBetMoney) {
		this.totalBetMoney = totalBetMoney;
	}

	 

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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