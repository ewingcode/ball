package com.ewing.order.ball.leaguelist;

public class League {

	private String league_id;
	private String game_count;
	private String league_name;

	public String getLeague_id() {
		return league_id;
	}

	public void setLeague_id(String league_id) {
		this.league_id = league_id;
	}

	public String getGame_count() {
		return game_count;
	}

	public void setGame_count(String game_count) {
		this.game_count = game_count;
	}

	public String getLeague_name() {
		return league_name;
	}

	public void setLeague_name(String league_name) {
		this.league_name = league_name;
	}

	@Override
	public String toString() {
		return "League [league_id=" + league_id + ", game_count=" + game_count + ", league_name="
				+ league_name + "]";
	}

}
