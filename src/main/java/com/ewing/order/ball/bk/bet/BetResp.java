package com.ewing.order.ball.bk.bet;

import java.io.Serializable;

import com.ewing.order.ball.shared.XMLBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("serverrequest")
public class BetResp extends XMLBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errormsg;
	private String errorvalue;
	private String code;
	private String ticket_id;
	private String gid;
	private String gtype;
	private String wtype;
	private String rtype;
	private String type;
	private String strong;
	private String ioratio;
	private String gold;
	private String concede;
	private String ratio;
	private String spread;
	private String date;
	private String time;
	private String team_id_h;
	private String team_id_c;
	private String league_id;
	private String maxcredit;
	private String mid;
	private String username;
	private String mtype;
	private String league;
	private String team_c;
	private String team_h;
	private String imp;
	private String ptype;
	private String ball_act;
	private String ms;
	private String timestamp;
	private String score_h;
	private String score_c;

	public static BetResp debugBetResp(){
		BetResp d = new BetResp();
		d.setErrormsg("test bet.");
		return d;
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

}
