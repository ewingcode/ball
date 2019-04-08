package com.ewing.order.busi.ball.ddl;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 自动投注买入账户信息
 * 
 * @author tanson lam
 * @create 2016年9月6日
 */
@Entity
@Table(name = "bet_autobuy")
public class BetAutoBuy implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BetAutoBuy() {
		super();
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
	@Column(name = "account")
	private String account;
	@Column(name = "pwd")
	private String pwd;
	@Column(name = "is_login")
	private String is_login;
	@Column(name = "phone")
	private String phone;
	@Column(name = "iseff")
	private String iseff;
	@Column(name = "isallow")
	private String isallow;
	@Column(name = "stop_byrule")
	private String stopByrule; 
	@Column(name = "createTime")
	private Timestamp createTime;
	@Column(name = "lastUpdate")
	private Timestamp lastUpdate;

	public String getStopByrule() {
		return stopByrule;
	}

	public void setStopByrule(String stopByrule) {
		this.stopByrule = stopByrule;
	}

	public String getIsallow() {
		return isallow;
	}

	public void setIsallow(String isallow) {
		this.isallow = isallow;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "BetAutoBuy [id=" + id + ", account=" + account + ", pwd=" + pwd + ", is_login="
				+ is_login + ", iseff=" + iseff +"]";
	}

	public String getIs_login() {
		return is_login;
	}

	public void setIs_login(String is_login) {
		this.is_login = is_login;
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

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
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