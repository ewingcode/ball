package com.ewing.order.ball.event;

import java.util.EventObject;

public class BallEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	private String gameId;

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public BallEvent(String gameId, Object source) {
		super(source);
		this.gameId = gameId;
	}

}