package com.ewing.order.ball.ft.game;

import java.io.Serializable;
import java.util.List;

import com.ewing.order.ball.shared.XMLBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 联赛赛程列表
 * 
 * @author tansonlam
 * @create 2018年7月21日
 */
@XStreamAlias("serverresponse")
public class FtGameListResp extends XMLBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String systime;
	@XStreamImplicit
	@XStreamAlias("game")
	private List<FtGame> game;

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

	public List<FtGame> getGame() {
		return game;
	}

	public void setGame(List<FtGame> game) {
		this.game = game;
	}

}
