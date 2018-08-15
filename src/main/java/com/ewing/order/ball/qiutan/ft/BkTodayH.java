package com.ewing.order.ball.qiutan.ft;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("h")
public class BkTodayH {

	private String content;

	private String gId;

	private String league;

	private String team_h;

	private String team_c;

	private List<BkRate> rateList;

	public List<BkRate> getRateList() {
		return rateList;
	}

	public void setRateList(List<BkRate> rateList) {
		this.rateList = rateList;
	}

	public String getgId() {
		return gId;
	}

	public void setgId(String gId) {
		this.gId = gId;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void toParse() {
		String[] s = StringUtils.split(content, "^");

		this.gId = s[0];
		this.league = s[1];
		this.team_h = s[7];
		this.team_c = s[9];
	}

	@Override
	public String toString() {
		return "BkTodayH [gId=" + gId + ", league=" + league + ", team_h=" + team_h + ", team_c="
				+ team_c + "]";
	}

}
