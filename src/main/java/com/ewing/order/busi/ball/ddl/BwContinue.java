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