package com.ewing.order.busi.ball.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author tansonlam
 * @create 2018年7月25日
 */
@Entity
@Table(name = "bet_log_result")
public class BetFullDetailDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BetFullDetailDto() {
		super();
	}
	//l.roll_id,r.t_count,r.sc_FT_A,r.sc_FT_H,r.ior_ROUH,r.ior_ROUC,i.sc_FT_A,i.sc_FT_H,
	@Column(name = "roll_id")
	private String rollId;
	@Column(name = "t_count")
	private String t_count;
	
	@Column(name = "sc_FT_A")
	private String sc_FT_A;
	@Column(name = "sc_FT_H")
	private String sc_FT_H;
	@Column(name = "ior_ROUH")
	private String ior_ROUH;
	@Column(name = "ior_ROUC")
	private String ior_ROUC;
	@Column(name = "end_sc_FT_A")
	private String end_sc_FT_A;
	@Column(name = "end_sc_FT_H")
	private String end_sc_FT_H;  
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
	@Column(name = "n_result")
	private String nResult;
	@Column(name = "match_status")
	private String matchStatus;
	@Column(name = "sc_total")
	private String sc_total;
	@Column(name = "total")
	private String total;
	@Column(name = "errormsg")
	private String errormsg;
	private String wingold;
	
	private String orgResult;
	private String orgNResult;
	
	
	public String getSc_total() {
		return sc_total;
	}
	public void setSc_total(String sc_total) {
		this.sc_total = sc_total;
	}
	public String getOrgNResult() {
		return orgNResult;
	}
	public void setOrgNResult(String orgNResult) {
		this.orgNResult = orgNResult;
	}
	public String getOrgResult() {
		return orgResult;
	}
	public void setOrgResult(String orgResult) {
		this.orgResult = orgResult;
	}
	public String getRollId() {
		return rollId;
	}
	public void setRollId(String rollId) {
		this.rollId = rollId;
	}
	public String getT_count() {
		return t_count;
	}
	public void setT_count(String t_count) {
		this.t_count = t_count;
	}
	public String getSc_FT_A() {
		return sc_FT_A;
	}
	public void setSc_FT_A(String sc_FT_A) {
		this.sc_FT_A = sc_FT_A;
	}
	public String getSc_FT_H() {
		return sc_FT_H;
	}
	public void setSc_FT_H(String sc_FT_H) {
		this.sc_FT_H = sc_FT_H;
	}
	public String getIor_ROUH() {
		return ior_ROUH;
	}
	public void setIor_ROUH(String ior_ROUH) {
		this.ior_ROUH = ior_ROUH;
	}
	public String getIor_ROUC() {
		return ior_ROUC;
	}
	public void setIor_ROUC(String ior_ROUC) {
		this.ior_ROUC = ior_ROUC;
	}
	public String getEnd_sc_FT_A() {
		return end_sc_FT_A;
	}
	public void setEnd_sc_FT_A(String end_sc_FT_A) {
		this.end_sc_FT_A = end_sc_FT_A;
	}
	public String getEnd_sc_FT_H() {
		return end_sc_FT_H;
	}
	public void setEnd_sc_FT_H(String end_sc_FT_H) {
		this.end_sc_FT_H = end_sc_FT_H;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public String getnResult() {
		return nResult;
	}
	public void setnResult(String nResult) {
		this.nResult = nResult;
	}
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
