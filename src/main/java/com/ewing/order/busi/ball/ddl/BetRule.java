package com.ewing.order.busi.ball.ddl;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 投注规则
 * 
 * @author tanson lam
 * @create 2016年9月6日
 */
@Entity
@Table(name = "bet_rule")
public class BetRule implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BetRule() {
		super();
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
	@Column(name = "account")
	private String account;
	@Column(name = "rule_pool_id")
	private Integer rulePoolId; 
	@Column(name = "name")
	private String name;
	@Column(name = "long_desc")
	private String long_desc;
	@Column(name = "gid")
	private String gid;
	@Column(name = "gtype")
	private String gtype;
	@Column(name = "ptype")
	private String ptype;
	@Column(name = "league")
	private String league;
	@Column(name = "team_h")
	private String team_h;
	@Column(name = "team_c")
	private String team_c;
	@Column(name = "param")
	private String param;
	@Column(name = "impl_code")
	private String impl_code;
	@Column(name = "comment")
	private String comment;
	@Column(name = "level")
	private String level;
	@Column(name = "bet_log_id")
	private Integer bet_log_id;
	@Column(name = "status")
	private String status;
	@Column(name = "iseff")
	private String iseff;
	@Column(name = "createTime")
	private Timestamp createTime;
	@Column(name = "lastUpdate")
	private Timestamp lastUpdate;
	@Column(name = "is_main")
	private String is_main;
	@Column(name = "money")
	private String money;
	@Column(name = "continue_max_match")
	private Integer continueMaxMatch; 
	@Column(name = "continue_start_lostnum")
	private Integer continueStartLostnum;
	@Column(name = "is_test")
	private Integer isTest;
	@Column(name = "stop_wingold")
	private Float stopWingold;
	@Column(name = "stop_losegold")
	private Float stopLosegold;
	@Column(name = "max_eachday")
	private Integer maxEachday;
	@Column(name = "is_cover")
	private Integer isCover;
	@Column(name = "continue_plan_money")
	private String continuePlanMoney;
	@Column(name = "win_rule")
	private String winRule; 
	//固定比例
	@Column(name = "pool_rate")
	private Float poolRate;
	//固定比例下注资金额池
	@Column(name = "rate_pool_money")
	private Float ratePoolMoney;
	//固定比例下注赢多少钱则停止下注
	@Column(name = "rate_stop_wingold")
	private Float rateStopWingold;
	//固定比例下注输多少钱则停止下注
	@Column(name = "rate_stop_losegold")
	private Float rateStopLosegold; 
 
	
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
 
	public String getWinRule() {
		return winRule;
	}

	public void setWinRule(String winRule) {
		this.winRule = winRule;
	}

	public Integer getMaxEachday() {
		return maxEachday;
	}

	public void setMaxEachday(Integer maxEachday) {
		this.maxEachday = maxEachday;
	}

	public Integer getRulePoolId() {
		return rulePoolId;
	}

	public void setRulePoolId(Integer rulePoolId) {
		this.rulePoolId = rulePoolId;
	}

	public String getContinuePlanMoney() {
		return continuePlanMoney;
	}

	public void setContinuePlanMoney(String continuePlanMoney) {
		this.continuePlanMoney = continuePlanMoney;
	}

	public Integer getIsCover() {
		return isCover;
	}

	public void setIsCover(Integer isCover) {
		this.isCover = isCover;
	}

	public Float getStopWingold() {
		return stopWingold;
	}

	public void setStopWingold(Float stopWingold) {
		this.stopWingold = stopWingold;
	}

	public Float getStopLosegold() {
		return stopLosegold;
	}

	public void setStopLosegold(Float stopLosegold) {
		this.stopLosegold = stopLosegold;
	}

	public Integer getContinueStartLostnum() {
		return continueStartLostnum;
	}

	public void setContinueStartLostnum(Integer continueStartLostnum) {
		this.continueStartLostnum = continueStartLostnum;
	}

	public Integer getIsTest() {
		return isTest;
	}

	public void setIsTest(Integer isTest) {
		this.isTest = isTest;
	}

	public Integer getContinueMaxMatch() {
		return continueMaxMatch;
	}

	public void setContinueMaxMatch(Integer continueMaxMatch) {
		this.continueMaxMatch = continueMaxMatch;
	}

	public String getIs_main() {
		return is_main;
	}

	public void setIs_main(String is_main) {
		this.is_main = is_main;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getLong_desc() {
		return long_desc;
	}

	public void setLong_desc(String long_desc) {
		this.long_desc = long_desc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getBet_log_id() {
		return bet_log_id;
	}

	public void setBet_log_id(Integer bet_log_id) {
		this.bet_log_id = bet_log_id;
	}

	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	@Override
	public String toString() {
		return "BetRule [id=" + id + ", account=" + account + ", name=" + name + ", gid=" + gid
				+ ", gtype=" + gtype + ", league=" + league + ", team_h=" + team_h + ", team_c="
				+ team_c + ", param=" + param + ", impl_code=" + impl_code + ", comment=" + comment
				+ ", level=" + level + ", iseff=" + iseff + ", createTime=" + createTime
				+ ", lastUpdate=" + lastUpdate + "]";
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}

	public String getTeam_h() {
		return team_h;
	}

	public void setTeam_h(String team_h) {
		this.team_h = team_h;
	}

	public String getTeam_c() {
		return team_c;
	}

	public void setTeam_c(String team_c) {
		this.team_c = team_c;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getImpl_code() {
		return impl_code;
	}

	public void setImpl_code(String impl_code) {
		this.impl_code = impl_code;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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