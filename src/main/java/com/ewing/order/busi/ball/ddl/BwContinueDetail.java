package com.ewing.order.busi.ball.ddl;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 投注批次明细表
 * 
 * @author tanson lam
 * @create 2016年9月6日
 */
@Entity
@Table(name = "bw_continue_detail")
public class BwContinueDetail implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BwContinueDetail() {
		super();
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
	@Column(name = "bw_continue_id")
	private Integer bwContinueId;
	@Column(name = "account")
	private String account;
	@Column(name = "bet_log_id")
	private Integer betLogId;
	@Column(name = "seq")
	private Integer seq;
	@Column(name = "before_pool_money")
	private Float beforePoolMoney;
	@Column(name = "pool_money")
	private Float poolMoney;
	@Column(name = "pool_rate")
	private Float pollRate;
	@Column(name = "bet_rate")
	private Float betRate;
	@Column(name = "bet_money")
	private Float betMoney;
	@Column(name = "win_gold")
	private Float winGold;
	@Column(name = "result")
	private String result;
	@Column(name = "createTime")
	private Timestamp createTime;
	@Column(name = "lastUpdate")
	private Timestamp lastUpdate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBwContinueId() {
		return bwContinueId;
	}

	public void setBwContinueId(Integer bwContinueId) {
		this.bwContinueId = bwContinueId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}  

	public Integer getBetLogId() {
		return betLogId;
	}

	public void setBetLogId(Integer betLogId) {
		this.betLogId = betLogId;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Float getBeforePoolMoney() {
		return beforePoolMoney;
	}

	public void setBeforePoolMoney(Float beforePoolMoney) {
		this.beforePoolMoney = beforePoolMoney;
	}

	public Float getPoolMoney() {
		return poolMoney;
	}

	public void setPoolMoney(Float poolMoney) {
		this.poolMoney = poolMoney;
	} 

	public Float getPollRate() {
		return pollRate;
	}

	public void setPollRate(Float pollRate) {
		this.pollRate = pollRate;
	}

	public Float getBetRate() {
		return betRate;
	}

	public void setBetRate(Float betRate) {
		this.betRate = betRate;
	}

	public Float getBetMoney() {
		return betMoney;
	}

	public void setBetMoney(Float betMoney) {
		this.betMoney = betMoney;
	}

	public Float getWinGold() {
		return winGold;
	}

	public void setWinGold(Float winGold) {
		this.winGold = winGold;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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