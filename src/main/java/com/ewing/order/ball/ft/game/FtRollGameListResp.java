package com.ewing.order.ball.ft.game;

import java.io.Serializable;
import java.util.List;

import com.ewing.order.ball.shared.XMLBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("serverresponse")
public class FtRollGameListResp extends XMLBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String systime;
	@XStreamImplicit
	@XStreamAlias("game")
	private List<FtRollGame> game;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSystime() {
		return systime;
	}

	public void setSystime(String systime) {
		this.systime = systime;
	}

	public List<FtRollGame> getGame() {
		return game;
	}

	public void setGame(List<FtRollGame> game) {
		this.game = game;
	}

}
