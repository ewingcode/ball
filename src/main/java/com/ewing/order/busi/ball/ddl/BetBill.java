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
@Table(name = "bet_bill")
public class BetBill implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BetBill() {
		super();
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	private String id;
	private String date;
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	private String account;
	private String w_id;
	private String addtime;
	private String oddf_type;
	private String gtype;
	private String w_ms;
	private String wtype;
	private String bet_wtype;
	private String league;
	private String team_h_show;
	private String team_c_show;
	private String team_h_ratio;
	private String team_c_ratio;
	private String ratio;
	private String org_score;
	private String score;
	private String result;
	private String pname;
	private String ioratio;
	private String result_data;
	private String ball_act_class;
	private String ball_act_ret;
	private String gold;
	private String win_gold;
	private String push;
	@Column(name = "createTime")
	private Timestamp createTime;
	@Column(name = "lastUpdate")
	private Timestamp lastUpdate;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getW_id() {
		return w_id;
	}

	public void setW_id(String w_id) {
		this.w_id = w_id;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getOddf_type() {
		return oddf_type;
	}

	public void setOddf_type(String oddf_type) {
		this.oddf_type = oddf_type;
	}

	public String getGtype() {
		return gtype;
	}

	public void setGtype(String gtype) {
		this.gtype = gtype;
	}

	public String getW_ms() {
		return w_ms;
	}

	public void setW_ms(String w_ms) {
		this.w_ms = w_ms;
	}

	public String getWtype() {
		return wtype;
	}

	public void setWtype(String wtype) {
		this.wtype = wtype;
	}

	public String getBet_wtype() {
		return bet_wtype;
	}

	public void setBet_wtype(String bet_wtype) {
		this.bet_wtype = bet_wtype;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}

	public String getTeam_h_show() {
		return team_h_show;
	}

	public void setTeam_h_show(String team_h_show) {
		this.team_h_show = team_h_show;
	}

	public String getTeam_c_show() {
		return team_c_show;
	}

	public void setTeam_c_show(String team_c_show) {
		this.team_c_show = team_c_show;
	}

	public String getTeam_h_ratio() {
		return team_h_ratio;
	}

	public void setTeam_h_ratio(String team_h_ratio) {
		this.team_h_ratio = team_h_ratio;
	}

	public String getTeam_c_ratio() {
		return team_c_ratio;
	}

	public void setTeam_c_ratio(String team_c_ratio) {
		this.team_c_ratio = team_c_ratio;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getOrg_score() {
		return org_score;
	}

	public void setOrg_score(String org_score) {
		this.org_score = org_score;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getIoratio() {
		return ioratio;
	}

	public void setIoratio(String ioratio) {
		this.ioratio = ioratio;
	}

	public String getResult_data() {
		return result_data;
	}

	public void setResult_data(String result_data) {
		this.result_data = result_data;
	}

	public String getBall_act_class() {
		return ball_act_class;
	}

	public void setBall_act_class(String ball_act_class) {
		this.ball_act_class = ball_act_class;
	}

	public String getBall_act_ret() {
		return ball_act_ret;
	}

	public void setBall_act_ret(String ball_act_ret) {
		this.ball_act_ret = ball_act_ret;
	}

	public String getGold() {
		return gold;
	}

	public void setGold(String gold) {
		this.gold = gold;
	}

	public String getWin_gold() {
		return win_gold;
	}

	public void setWin_gold(String win_gold) {
		this.win_gold = win_gold;
	}

	public String getPush() {
		return push;
	}

	public void setPush(String push) {
		this.push = push;
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
