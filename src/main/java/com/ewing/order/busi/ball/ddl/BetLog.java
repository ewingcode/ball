package com.ewing.order.busi.ball.ddl;

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
@Table(name = "bet_log")
public class BetLog implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BetLog() {
		super();
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
	@Column(name = "bet_rule_id")
	private Integer bet_rule_id;
	@Column(name = "account")
	private String account;
	@Column(name = "ball_account")
	private String ballAccount;
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
	@Column(name = "time")
	private String time;
	@Column(name = "team_id_h")
	private String team_id_h;
	@Column(name = "team_id_c")
	private String team_id_c;
	@Column(name = "league_id")
	private String league_id;
	@Column(name = "maxcredit")
	private String maxcredit;
	@Column(name = "mid")
	private String mid;
	@Column(name = "username")
	private String username;
	@Column(name = "mtype")
	private String mtype;
	@Column(name = "league")
	private String league;
	@Column(name = "team_c")
	private String team_c;
	@Column(name = "team_h")
	private String team_h;
	@Column(name = "imp")
	private String imp;
	@Column(name = "ptype")
	private String ptype;
	@Column(name = "ball_act")
	private String ball_act;
	@Column(name = "ms")
	private String ms;
	@Column(name = "timestamp")
	private String timestamp;
	@Column(name = "errormsg")
	private String errormsg;
	@Column(name = "errorvalue")
	private String errorvalue;
	@Column(name = "createTime")
	private Timestamp createTime;
	@Column(name = "lastUpdate")
	private Timestamp lastUpdate;
	@Column(name = "score_h")
	private String score_h;
	@Column(name = "score_c")
	private String score_c;
	@Column(name = "buy_desc")
	private String buy_desc; 
	@Column(name = "is_notify")
	private String is_notify; 
	
	 
	public String getBallAccount() {
		return ballAccount;
	}

	public void setBallAccount(String ballAccount) {
		this.ballAccount = ballAccount;
	}

	public String getIs_notify() {
		return is_notify;
	}

	public void setIs_notify(String is_notify) {
		this.is_notify = is_notify;
	}

	public String getBuy_desc() {
		return buy_desc;
	}

	public void setBuy_desc(String buy_desc) {
		this.buy_desc = buy_desc;
	}

	public Integer getBet_rule_id() {
		return bet_rule_id;
	}

	public void setBet_rule_id(Integer bet_rule_id) {
		this.bet_rule_id = bet_rule_id;
	}

	public String getScore_h() {
		return score_h;
	}

	public void setScore_h(String score_h) {
		this.score_h = score_h;
	}

	public String getScore_c() {
		return score_c;
	}

	public void setScore_c(String score_c) {
		this.score_c = score_c;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTeam_id_h() {
		return team_id_h;
	}

	public void setTeam_id_h(String team_id_h) {
		this.team_id_h = team_id_h;
	}

	public String getTeam_id_c() {
		return team_id_c;
	}

	public void setTeam_id_c(String team_id_c) {
		this.team_id_c = team_id_c;
	}

	public String getLeague_id() {
		return league_id;
	}

	public void setLeague_id(String league_id) {
		this.league_id = league_id;
	}

	public String getMaxcredit() {
		return maxcredit;
	}

	public void setMaxcredit(String maxcredit) {
		this.maxcredit = maxcredit;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMtype() {
		return mtype;
	}

	public void setMtype(String mtype) {
		this.mtype = mtype;
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

	public String getImp() {
		return imp;
	}

	public void setImp(String imp) {
		this.imp = imp;
	}

	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	public String getBall_act() {
		return ball_act;
	}

	public void setBall_act(String ball_act) {
		this.ball_act = ball_act;
	}

	public String getMs() {
		return ms;
	}

	public void setMs(String ms) {
		this.ms = ms;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getErrorvalue() {
		return errorvalue;
	}

	public void setErrorvalue(String errorvalue) {
		this.errorvalue = errorvalue;
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
