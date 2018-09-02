package com.ewing.order.busi.ball.ddl;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 比赛投注信息
 * 
 * @author tanson lam
 * @create 2016年9月6日
 */
@Entity
@Table(name = "bet_roll_info")
public class BetRollInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BetRollInfo() {
		super();
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
	@Column(name = "gtype")
	private String gtype;
	@Column(name = "gid")
	private String gid;
	@Column(name = "gidm")
	private String gidm;
	@Column(name = "systime")
	private String systime;
	@Column(name = "datetime")
	private String datetime;
	@Column(name = "re_time")
	private String re_time;
	@Column(name = "league")
	private String league;
	@Column(name = "team_h")
	private String team_h;
	@Column(name = "team_c")
	private String team_c;
	@Column(name = "gnum_c")
	private String gnum_c;
	@Column(name = "gnum_h")
	private String gnum_h;
	@Column(name = "gopen")
	private String gopen;
	@Column(name = "recv")
	private String recv;
	@Column(name = "strong")
	private String strong;
	@Column(name = "sw_RE")
	private String sw_RE;
	@Column(name = "ratio_re_c")
	private Float ratio_re_c;
	@Column(name = "ratio_re")
	private String ratio_re;
	@Column(name = "ior_REH")
	private String ior_REH;
	@Column(name = "ior_REC")
	private String ior_REC;
	@Column(name = "sw_ROU")
	private String sw_ROU;
	@Column(name = "ratio_rou_c")
	private Float ratio_rou_c;
	@Column(name = "ratio_rouo")
	private String ratio_rouo;
	@Column(name = "ratio_rouu")
	private String ratio_rouu;
	@Column(name = "ior_ROUH")
	private String ior_ROUH;
	@Column(name = "ior_ROUC")
	private String ior_ROUC;
	@Column(name = "hgid")
	private String hgid;
	@Column(name = "hgopen")
	private String hgopen;
	@Column(name = "hrecv")
	private String hrecv;
	@Column(name = "sw_HRE")
	private String sw_HRE;
	@Column(name = "ratio_hre")
	private String ratio_hre;
	@Column(name = "ior_HREH")
	private String ior_HREH;
	@Column(name = "ior_HREC")
	private String ior_HREC;
	@Column(name = "sw_HROU")
	private String sw_HROU;
	@Column(name = "ratio_hrouo")
	private String ratio_hrouo;
	@Column(name = "ratio_hrouu")
	private String ratio_hrouu;
	@Column(name = "ior_HROUH")
	private String ior_HROUH;
	@Column(name = "ior_HROUC")
	private String ior_HROUC;
	@Column(name = "score_h")
	private String score_h;
	@Column(name = "score_c")
	private String score_c;
	@Column(name = "createTime")
	private Timestamp createTime;
	@Column(name = "lastUpdate")
	private Timestamp lastUpdate;
	@Column(name = "sc_FT_A")
	private String sc_FT_A;
	@Column(name = "sc_FT_H")
	private String sc_FT_H;
	@Column(name = "sc_OT_A")
	private String sc_OT_A;
	@Column(name = "sc_OT_H")
	private String sc_OT_H;
	@Column(name = "sc_H2_A")
	private String sc_H2_A;
	@Column(name = "sc_H2_H")
	private String sc_H2_H;
	@Column(name = "sc_H1_A")
	private String sc_H1_A;
	@Column(name = "sc_H1_H")
	private String sc_H1_H;
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
	@Column(name = "half_time")
	private String HalfTime;
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
	@Column(name = "ptype")
	private String ptype;

	public Float getRatio_re_c() {
		return ratio_re_c;
	}

	public void setRatio_re_c(Float ratio_re_c) {
		this.ratio_re_c = ratio_re_c;
	}

	public Float getRatio_rou_c() {
		return ratio_rou_c;
	}

	public void setRatio_rou_c(Float ratio_rou_c) {
		this.ratio_rou_c = ratio_rou_c;
	}

	public String getRe_time() {
		return re_time;
	}

	public void setRe_time(String re_time) {
		this.re_time = re_time;
	}

	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	public String getSystime() {
		return systime;
	}

	public void setSystime(String systime) {
		this.systime = systime;
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

	public String getSc_H2_A() {
		return sc_H2_A;
	}

	public void setSc_H2_A(String sc_H2_A) {
		this.sc_H2_A = sc_H2_A;
	}

	public String getSc_H2_H() {
		return sc_H2_H;
	}

	public void setSc_H2_H(String sc_H2_H) {
		this.sc_H2_H = sc_H2_H;
	}

	public String getSc_H1_A() {
		return sc_H1_A;
	}

	public void setSc_H1_A(String sc_H1_A) {
		this.sc_H1_A = sc_H1_A;
	}

	public String getSc_H1_H() {
		return sc_H1_H;
	}

	public void setSc_H1_H(String sc_H1_H) {
		this.sc_H1_H = sc_H1_H;
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

	public String getHalfTime() {
		return HalfTime;
	}

	public void setHalfTime(String halfTime) {
		HalfTime = halfTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getGnum_c() {
		return gnum_c;
	}

	public void setGnum_c(String gnum_c) {
		this.gnum_c = gnum_c;
	}

	public String getGnum_h() {
		return gnum_h;
	}

	public void setGnum_h(String gnum_h) {
		this.gnum_h = gnum_h;
	}

	public String getGopen() {
		return gopen;
	}

	public void setGopen(String gopen) {
		this.gopen = gopen;
	}

	public String getRecv() {
		return recv;
	}

	public void setRecv(String recv) {
		this.recv = recv;
	}

	public String getStrong() {
		return strong;
	}

	public void setStrong(String strong) {
		this.strong = strong;
	}

	public String getSw_RE() {
		return sw_RE;
	}

	public void setSw_RE(String sw_RE) {
		this.sw_RE = sw_RE;
	}

	public String getRatio_re() {
		return ratio_re;
	}

	public void setRatio_re(String ratio_re) {
		this.ratio_re = ratio_re;
	}

	public String getIor_REH() {
		return ior_REH;
	}

	public void setIor_REH(String ior_REH) {
		this.ior_REH = ior_REH;
	}

	public String getIor_REC() {
		return ior_REC;
	}

	public void setIor_REC(String ior_REC) {
		this.ior_REC = ior_REC;
	}

	public String getSw_ROU() {
		return sw_ROU;
	}

	public void setSw_ROU(String sw_ROU) {
		this.sw_ROU = sw_ROU;
	}

	public String getRatio_rouo() {
		return ratio_rouo;
	}

	public void setRatio_rouo(String ratio_rouo) {
		this.ratio_rouo = ratio_rouo;
	}

	public String getRatio_rouu() {
		return ratio_rouu;
	}

	public void setRatio_rouu(String ratio_rouu) {
		this.ratio_rouu = ratio_rouu;
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

	public String getHgid() {
		return hgid;
	}

	public void setHgid(String hgid) {
		this.hgid = hgid;
	}

	public String getHgopen() {
		return hgopen;
	}

	public void setHgopen(String hgopen) {
		this.hgopen = hgopen;
	}

	public String getHrecv() {
		return hrecv;
	}

	public void setHrecv(String hrecv) {
		this.hrecv = hrecv;
	}

	public String getSw_HRE() {
		return sw_HRE;
	}

	public void setSw_HRE(String sw_HRE) {
		this.sw_HRE = sw_HRE;
	}

	public String getRatio_hre() {
		return ratio_hre;
	}

	public void setRatio_hre(String ratio_hre) {
		this.ratio_hre = ratio_hre;
	}

	public String getIor_HREH() {
		return ior_HREH;
	}

	public void setIor_HREH(String ior_HREH) {
		this.ior_HREH = ior_HREH;
	}

	public String getIor_HREC() {
		return ior_HREC;
	}

	public void setIor_HREC(String ior_HREC) {
		this.ior_HREC = ior_HREC;
	}

	public String getSw_HROU() {
		return sw_HROU;
	}

	public void setSw_HROU(String sw_HROU) {
		this.sw_HROU = sw_HROU;
	}

	public String getRatio_hrouo() {
		return ratio_hrouo;
	}

	public void setRatio_hrouo(String ratio_hrouo) {
		this.ratio_hrouo = ratio_hrouo;
	}

	public String getRatio_hrouu() {
		return ratio_hrouu;
	}

	public void setRatio_hrouu(String ratio_hrouu) {
		this.ratio_hrouu = ratio_hrouu;
	}

	public String getIor_HROUH() {
		return ior_HROUH;
	}

	public void setIor_HROUH(String ior_HROUH) {
		this.ior_HROUH = ior_HROUH;
	}

	public String getIor_HROUC() {
		return ior_HROUC;
	}

	public void setIor_HROUC(String ior_HROUC) {
		this.ior_HROUC = ior_HROUC;
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

	public boolean isSame(BetRollInfo betRollInfo) {
		return betRollInfo.getIor_HREC().equals(this.getIor_HREC())
				&& betRollInfo.getIor_HREH().equals(this.getIor_HREH())
				&& betRollInfo.getIor_HROUC().equals(this.getIor_HROUC())
				&& betRollInfo.getIor_HROUH().equals(this.getIor_HROUH())
				&& betRollInfo.getIor_REC().equals(this.getIor_REC())
				&& betRollInfo.getIor_REH().equals(this.getIor_REH())
				&& betRollInfo.getIor_ROUC().equals(this.getIor_ROUC())
				&& betRollInfo.getIor_ROUH().equals(this.getIor_ROUH())
				&& betRollInfo.getRatio_hre().equals(this.getRatio_hre())
				&& betRollInfo.getRatio_hrouo().equals(this.getRatio_hrouo())
				&& betRollInfo.getRatio_hrouu().equals(this.getRatio_hrouu())
				&& betRollInfo.getRatio_re().equals(this.getRatio_re())
				&& betRollInfo.getRatio_rouo().equals(this.getRatio_rouo())
				&& betRollInfo.getRatio_rouu().equals(this.getRatio_rouu());
	}

}