package com.ewing.order.ball.bk.bet;

import java.io.Serializable;

import com.ewing.order.ball.shared.XMLBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author tansonlam
 * @create 2018年7月23日 
 */
@XStreamAlias("serverrequest")
public class BkPreOrderViewResp extends XMLBean implements Serializable { 
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errormsg;
	private String code;
	private String game_sc;
	private String game_so;
	private String mem_sc;
	private String mem_so;
	private String gold_gmin;
	private String gold_gmax;
	private String maxcredit;
	private String con;
	private String ratio;
	//投注回报
	private String ioratio;
	//让分
	private String spread;
	//强弱方 H主场 C客场
	private String strong;
	private String restsinglecredit;
	private String league_id;
	private String dates;
	private String times;
	private String num_c;
	private String num_h;
	private String team_id_c;
	private String team_id_h;
	private String currency;
	private String currency_value;
	private String ltype;
	private String pay_type;
	private String username;
	private String aid;
	private String ms;
	private String league_name;
	private String team_name_c;
	private String team_name_h;
	private String score;
	private String max_gold;
	//交易时间戳
	private String ts;
	private String dg;
	
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getGame_sc() {
		return game_sc;
	}
	public void setGame_sc(String game_sc) {
		this.game_sc = game_sc;
	}
	public String getGame_so() {
		return game_so;
	}
	public void setGame_so(String game_so) {
		this.game_so = game_so;
	}
	public String getMem_sc() {
		return mem_sc;
	}
	public void setMem_sc(String mem_sc) {
		this.mem_sc = mem_sc;
	}
	public String getMem_so() {
		return mem_so;
	}
	public void setMem_so(String mem_so) {
		this.mem_so = mem_so;
	}
	public String getGold_gmin() {
		return gold_gmin;
	}
	public void setGold_gmin(String gold_gmin) {
		this.gold_gmin = gold_gmin;
	}
	public String getGold_gmax() {
		return gold_gmax;
	}
	public void setGold_gmax(String gold_gmax) {
		this.gold_gmax = gold_gmax;
	}
	public String getMaxcredit() {
		return maxcredit;
	}
	public void setMaxcredit(String maxcredit) {
		this.maxcredit = maxcredit;
	}
	public String getCon() {
		return con;
	}
	public void setCon(String con) {
		this.con = con;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public String getIoratio() {
		return ioratio;
	}
	public void setIoratio(String ioratio) {
		this.ioratio = ioratio;
	}
	public String getSpread() {
		return spread;
	}
	public void setSpread(String spread) {
		this.spread = spread;
	}
	public String getStrong() {
		return strong;
	}
	public void setStrong(String strong) {
		this.strong = strong;
	}
	public String getRestsinglecredit() {
		return restsinglecredit;
	}
	public void setRestsinglecredit(String restsinglecredit) {
		this.restsinglecredit = restsinglecredit;
	}
	public String getLeague_id() {
		return league_id;
	}
	public void setLeague_id(String league_id) {
		this.league_id = league_id;
	}
	public String getDates() {
		return dates;
	}
	public void setDates(String dates) {
		this.dates = dates;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public String getNum_c() {
		return num_c;
	}
	public void setNum_c(String num_c) {
		this.num_c = num_c;
	}
	public String getNum_h() {
		return num_h;
	}
	public void setNum_h(String num_h) {
		this.num_h = num_h;
	}
	public String getTeam_id_c() {
		return team_id_c;
	}
	public void setTeam_id_c(String team_id_c) {
		this.team_id_c = team_id_c;
	}
	public String getTeam_id_h() {
		return team_id_h;
	}
	public void setTeam_id_h(String team_id_h) {
		this.team_id_h = team_id_h;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrency_value() {
		return currency_value;
	}
	public void setCurrency_value(String currency_value) {
		this.currency_value = currency_value;
	}
	public String getLtype() {
		return ltype;
	}
	public void setLtype(String ltype) {
		this.ltype = ltype;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getMs() {
		return ms;
	}
	public void setMs(String ms) {
		this.ms = ms;
	}
	public String getLeague_name() {
		return league_name;
	}
	public void setLeague_name(String league_name) {
		this.league_name = league_name;
	}
	public String getTeam_name_c() {
		return team_name_c;
	}
	public void setTeam_name_c(String team_name_c) {
		this.team_name_c = team_name_c;
	}
	public String getTeam_name_h() {
		return team_name_h;
	}
	public void setTeam_name_h(String team_name_h) {
		this.team_name_h = team_name_h;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getMax_gold() {
		return max_gold;
	}
	public void setMax_gold(String max_gold) {
		this.max_gold = max_gold;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public String getDg() {
		return dg;
	}
	public void setDg(String dg) {
		this.dg = dg;
	} 
}
