package com.ewing.order.busi.ball.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author tansonlam
 * @create 2018年7月25日
 */
@Entity
@Table(name = "bet_log_result")
public class BetDetailDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BetDetailDto() {
		super();
	}
 
	@Column(name = "id")
	private Integer id; 
	@Column(name = "account")
	private String account;
	@Column(name = "code")
	private String code;
	@Column(name = "ticket_id")
	private String ticket_id;
	@Column(name = "gid")
	private String gid;
	@Column(name = "gtype")
	private String gtype;
	@Column(name = "wtype")
	private String wtype;
	@Column(name = "rtype")
	private String rtype;
	@Column(name = "type")
	private String type;
	@Column(name = "strong")
	private String strong;
	@Column(name = "ioratio")
	private String ioratio;
	@Column(name = "gold")
	private String gold;
	@Column(name = "concede")
	private String concede;
	@Column(name = "ratio")
	private String ratio;
	@Column(name = "spread")
	private String spread;
	@Column(name = "date")
	private String date;  
	@Column(name = "username")
	private String username; 
	@Column(name = "league")
	private String league;
	@Column(name = "team_c")
	private String team_c;
	@Column(name = "team_h")
	private String team_h;  
	@Column(name = "createTime")
	private String createTime; 
	@Column(name = "result")
	private String result;
	@Column(name = "match_status")
	private String matchStatus;
	@Column(name = "total")
	private String total;

	private String wingold;
	
	public String getWingold() {
		return wingold;
	}
	public void setWingold(String wingold) {
		this.wingold = wingold;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTicket_id() {
		return ticket_id;
	}
	public void setTicket_id(String ticket_id) {
		this.ticket_id = ticket_id;
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
	public String getWtype() {
		return wtype;
	}
	public void setWtype(String wtype) {
		this.wtype = wtype;
	}
	public String getRtype() {
		return rtype;
	}
	public void setRtype(String rtype) {
		this.rtype = rtype;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStrong() {
		return strong;
	}
	public void setStrong(String strong) {
		this.strong = strong;
	}
	public String getIoratio() {
		return ioratio;
	}
	public void setIoratio(String ioratio) {
		this.ioratio = ioratio;
	}
	public String getGold() {
		return gold;
	}
	public void setGold(String gold) {
		this.gold = gold;
	}
	public String getConcede() {
		return concede;
	}
	public void setConcede(String concede) {
		this.concede = concede;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public String getSpread() {
		return spread;
	}
	public void setSpread(String spread) {
		this.spread = spread;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLeague() {
		return league;
	}
	public void setLeague(String league) {
		this.league = league;
	}
	public String getTeam_c() {
		return team_c;
	}
	public void setTeam_c(String team_c) {
		this.team_c = team_c;
	}
	public String getTeam_h() {
		return team_h;
	}
	public void setTeam_h(String team_h) {
		this.team_h = team_h;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMatchStatus() {
		return matchStatus;
	}
	public void setMatchStatus(String matchStatus) {
		this.matchStatus = matchStatus;
	}
	 
}
