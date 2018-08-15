package com.ewing.order.ball.leaguelist;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("game") 
public class LeagueType {

	private String id;
	private String gtype;
	@XStreamImplicit
	@XStreamAlias("league")
	private List<League> league;

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

	public List<League> getLeague() {
		return league;
	}

	public void setLeague(List<League> league) {
		this.league = league;
	}

}
