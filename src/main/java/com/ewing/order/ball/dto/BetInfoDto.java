package com.ewing.order.ball.dto;

import javax.persistence.Column;

/**
 * 比赛投注信息
 * 
 * @author tanson lam
 * @create 2016年9月6日
 */ 
public class BetInfoDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BetInfoDto() {
		super();
	}
 
	@Column(name = "id")
	private String id;
	@Column(name = "gtype")
	private String gtype;
	@Column(name = "gid")
	private String gid;
	@Column(name = "gidm")
	private String gidm;
	@Column(name = "systime")
	private String systime;
	@Column(name = "re_time")
	private String re_time;
	@Column(name = "datetime")
	private String datetime; 
	private String matchtime;
	@Column(name = "league")
	private String league;
	@Column(name = "team_h")
	private String team_h;
	@Column(name = "team_c")
	private String team_c;
	@Column(name = "strong")
	private String strong;
	@Column(name = "sw_R")
	private String sw_R;
	@Column(name = "ratio")
	private String ratio;
	@Column(name = "ior_RH")
	private String ior_RH;
	@Column(name = "ior_RC")
	private String ior_RC;
	@Column(name = "sw_OU")
	private String sw_OU;
	@Column(name = "ratio_o")
	private String ratio_o;
	@Column(name = "ratio_u")
	private String ratio_u;
	@Column(name = "ior_OUH")
	private String ior_OUH;
	@Column(name = "ior_OUC")
	private String ior_OUC;
	@Column(name = "n_strong")
	private String n_strong;
	@Column(name = "n_sw_R")
	private String n_sw_R;
	@Column(name = "n_ratio")
	private String n_ratio;
	@Column(name = "n_ior_RH")
	private String n_ior_RH;
	@Column(name = "n_ior_RC")
	private String n_ior_RC;
	@Column(name = "n_sw_OU")
	private String n_sw_OU;
	@Column(name = "n_ratio_o")
	private String n_ratio_o;
	@Column(name = "n_ratio_u")
	private String n_ratio_u;
	@Column(name = "n_ior_OUH")
	private String n_ior_OUH;
	@Column(name = "n_ior_OUC")
	private String n_ior_OUC;
	@Column(name = "sc_total")
	private String sc_total;
	@Column(name = "rou_dis")
	private String rou_dis;
	@Column(name = "re_dis")
	private String re_dis;
	@Column(name = "sc_Q4_total")
	private String sc_Q4_total;
	@Column(name = "sc_Q3_total")
	private String sc_Q3_total;
	@Column(name = "sc_Q2_total")
	private String sc_Q2_total;
	@Column(name = "sc_Q1_total")
	private String sc_Q1_total;
	@Column(name = "sc_Q4_A")
	private String sc_Q4_A;
	@Column(name = "sc_Q4_H")
	private String sc_Q4_H;
	@Column(name = "sc_Q3_A")
	private String sc_Q3_A;
	@Column(name = "sc_Q3_H")
	private String sc_Q3_H;
	@Column(name = "sc_Q2_A")
	private String sc_Q2_A;
	@Column(name = "sc_Q2_H")
	private String sc_Q2_H;
	@Column(name = "sc_Q1_A")
	private String sc_Q1_A;
	@Column(name = "sc_Q1_H")
	private String sc_Q1_H;
	@Column(name = "se_now")
	private String se_now;
	@Column(name = "se_type")
	private String se_type;
	@Column(name = "t_status")
	private String t_status;
	@Column(name = "t_count")
	private String t_count;
	@Column(name = "sc_new")
	private String sc_new;
	@Column(name = "ptype")
	private String ptype;
	@Column(name = "score_h")
	private String score_h;
	@Column(name = "score_c")
	private String score_c;
	@Column(name = "status")
	private String status;
	@Column(name = "sc_FT_A")
	private String sc_FT_A;
	@Column(name = "sc_FT_H")
	private String sc_FT_H;
	@Column(name = "sc_OT_A")
	private String sc_OT_A;
	@Column(name = "sc_OT_H")
	private String sc_OT_H;
	@Column(name = "minRatioR")
	private Float minRatioR;
	@Column(name = "maxRatioR")
	private Float maxRatioR;
	@Column(name = "minRatioRou")
	private Float minRatioRou;
	@Column(name = "maxRatioRou")
	private Float maxRatioRou;

	public String getMatchtime() {
		return matchtime;
	}

	public void setMatchtime(String matchtime) {
		this.matchtime = matchtime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGtype() {
		return gtype;
	}

	public void setGtype(String gtype) {
		this.gtype = gtype;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getGidm() {
		return gidm;
	}

	public void setGidm(String gidm) {
		this.gidm = gidm;
	}

	public String getSystime() {
		return systime;
	}

	public void setSystime(String systime) {
		this.systime = systime;
	}

	public String getRe_time() {
		return re_time;
	}

	public void setRe_time(String re_time) {
		this.re_time = re_time;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
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

	public String getStrong() {
		return strong;
	}

	public void setStrong(String strong) {
		this.strong = strong;
	}

	public String getSw_R() {
		return sw_R;
	}

	public void setSw_R(String sw_R) {
		this.sw_R = sw_R;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getIor_RH() {
		return ior_RH;
	}

	public void setIor_RH(String ior_RH) {
		this.ior_RH = ior_RH;
	}

	public String getIor_RC() {
		return ior_RC;
	}

	public void setIor_RC(String ior_RC) {
		this.ior_RC = ior_RC;
	}

	public String getSw_OU() {
		return sw_OU;
	}

	public void setSw_OU(String sw_OU) {
		this.sw_OU = sw_OU;
	}

	public String getRatio_o() {
		return ratio_o;
	}

	public void setRatio_o(String ratio_o) {
		this.ratio_o = ratio_o;
	}

	public String getRatio_u() {
		return ratio_u;
	}

	public void setRatio_u(String ratio_u) {
		this.ratio_u = ratio_u;
	}

	public String getIor_OUH() {
		return ior_OUH;
	}

	public void setIor_OUH(String ior_OUH) {
		this.ior_OUH = ior_OUH;
	}

	public String getIor_OUC() {
		return ior_OUC;
	}

	public void setIor_OUC(String ior_OUC) {
		this.ior_OUC = ior_OUC;
	}

	public String getN_strong() {
		return n_strong;
	}

	public void setN_strong(String n_strong) {
		this.n_strong = n_strong;
	}

	public String getN_sw_R() {
		return n_sw_R;
	}

	public void setN_sw_R(String n_sw_R) {
		this.n_sw_R = n_sw_R;
	}

	public String getN_ratio() {
		return n_ratio;
	}

	public void setN_ratio(String n_ratio) {
		this.n_ratio = n_ratio;
	}

	public String getN_ior_RH() {
		return n_ior_RH;
	}

	public void setN_ior_RH(String n_ior_RH) {
		this.n_ior_RH = n_ior_RH;
	}

	public String getN_ior_RC() {
		return n_ior_RC;
	}

	public void setN_ior_RC(String n_ior_RC) {
		this.n_ior_RC = n_ior_RC;
	}

	public String getN_sw_OU() {
		return n_sw_OU;
	}

	public void setN_sw_OU(String n_sw_OU) {
		this.n_sw_OU = n_sw_OU;
	}

	public String getN_ratio_o() {
		return n_ratio_o;
	}

	public void setN_ratio_o(String n_ratio_o) {
		this.n_ratio_o = n_ratio_o;
	}

	public String getN_ratio_u() {
		return n_ratio_u;
	}

	public void setN_ratio_u(String n_ratio_u) {
		this.n_ratio_u = n_ratio_u;
	}

	public String getN_ior_OUH() {
		return n_ior_OUH;
	}

	public void setN_ior_OUH(String n_ior_OUH) {
		this.n_ior_OUH = n_ior_OUH;
	}

	public String getN_ior_OUC() {
		return n_ior_OUC;
	}

	public void setN_ior_OUC(String n_ior_OUC) {
		this.n_ior_OUC = n_ior_OUC;
	}

	public String getSc_total() {
		return sc_total;
	}

	public void setSc_total(String sc_total) {
		this.sc_total = sc_total;
	}

	public String getRou_dis() {
		return rou_dis;
	}

	public void setRou_dis(String rou_dis) {
		this.rou_dis = rou_dis;
	}

	public String getRe_dis() {
		return re_dis;
	}

	public void setRe_dis(String re_dis) {
		this.re_dis = re_dis;
	}

	public String getSc_Q4_total() {
		return sc_Q4_total;
	}

	public void setSc_Q4_total(String sc_Q4_total) {
		this.sc_Q4_total = sc_Q4_total;
	}

	public String getSc_Q3_total() {
		return sc_Q3_total;
	}

	public void setSc_Q3_total(String sc_Q3_total) {
		this.sc_Q3_total = sc_Q3_total;
	}

	public String getSc_Q2_total() {
		return sc_Q2_total;
	}

	public void setSc_Q2_total(String sc_Q2_total) {
		this.sc_Q2_total = sc_Q2_total;
	}

	public String getSc_Q1_total() {
		return sc_Q1_total;
	}

	public void setSc_Q1_total(String sc_Q1_total) {
		this.sc_Q1_total = sc_Q1_total;
	}

	public String getSc_Q4_A() {
		return sc_Q4_A;
	}

	public void setSc_Q4_A(String sc_Q4_A) {
		this.sc_Q4_A = sc_Q4_A;
	}

	public String getSc_Q4_H() {
		return sc_Q4_H;
	}

	public void setSc_Q4_H(String sc_Q4_H) {
		this.sc_Q4_H = sc_Q4_H;
	}

	public String getSc_Q3_A() {
		return sc_Q3_A;
	}

	public void setSc_Q3_A(String sc_Q3_A) {
		this.sc_Q3_A = sc_Q3_A;
	}

	public String getSc_Q3_H() {
		return sc_Q3_H;
	}

	public void setSc_Q3_H(String sc_Q3_H) {
		this.sc_Q3_H = sc_Q3_H;
	}

	public String getSc_Q2_A() {
		return sc_Q2_A;
	}

	public void setSc_Q2_A(String sc_Q2_A) {
		this.sc_Q2_A = sc_Q2_A;
	}

	public String getSc_Q2_H() {
		return sc_Q2_H;
	}

	public void setSc_Q2_H(String sc_Q2_H) {
		this.sc_Q2_H = sc_Q2_H;
	}

	public String getSc_Q1_A() {
		return sc_Q1_A;
	}

	public void setSc_Q1_A(String sc_Q1_A) {
		this.sc_Q1_A = sc_Q1_A;
	}

	public String getSc_Q1_H() {
		return sc_Q1_H;
	}

	public void setSc_Q1_H(String sc_Q1_H) {
		this.sc_Q1_H = sc_Q1_H;
	}

	public String getSe_now() {
		return se_now;
	}

	public void setSe_now(String se_now) {
		this.se_now = se_now;
	}

	public String getSe_type() {
		return se_type;
	}

	public void setSe_type(String se_type) {
		this.se_type = se_type;
	}

	public String getT_status() {
		return t_status;
	}

	public void setT_status(String t_status) {
		this.t_status = t_status;
	}

	public String getT_count() {
		return t_count;
	}

	public void setT_count(String t_count) {
		this.t_count = t_count;
	}

	public String getSc_new() {
		return sc_new;
	}

	public void setSc_new(String sc_new) {
		this.sc_new = sc_new;
	}

	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getSc_OT_A() {
		return sc_OT_A;
	}

	public void setSc_OT_A(String sc_OT_A) {
		this.sc_OT_A = sc_OT_A;
	}

	public String getSc_OT_H() {
		return sc_OT_H;
	}

	public void setSc_OT_H(String sc_OT_H) {
		this.sc_OT_H = sc_OT_H;
	}

	public Float getMinRatioR() {
		return minRatioR;
	}

	public void setMinRatioR(Float minRatioR) {
		this.minRatioR = minRatioR;
	}

	public Float getMaxRatioR() {
		return maxRatioR;
	}

	public void setMaxRatioR(Float maxRatioR) {
		this.maxRatioR = maxRatioR;
	}

	public Float getMinRatioRou() {
		return minRatioRou;
	}

	public void setMinRatioRou(Float minRatioRou) {
		this.minRatioRou = minRatioRou;
	}

	public Float getMaxRatioRou() {
		return maxRatioRou;
	}

	public void setMaxRatioRou(Float maxRatioRou) {
		this.maxRatioRou = maxRatioRou;
	}

}