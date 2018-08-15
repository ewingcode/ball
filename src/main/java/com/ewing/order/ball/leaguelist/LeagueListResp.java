package com.ewing.order.ball.leaguelist;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.ewing.order.ball.shared.XMLBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("serverresponse")
public class LeagueListResp extends XMLBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String systime;
	@XStreamImplicit
	@XStreamAlias("sel_date")
	private List<SelDate> selDate;
	@XStreamImplicit
	@XStreamAlias("game")
	private List<LeagueType> game;

	public List<LeagueType> getGame() {
		return game;
	}

	public void setGame(List<LeagueType> game) {
		this.game = game;
	}

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

	public List<SelDate> getSelDate() {
		return selDate;
	}

	public void setSelDate(List<SelDate> selDate) {
		this.selDate = selDate;
	}
	
	public static void main(String[] args) throws IOException {
		String xmlStr = FileUtils.readFileToString(new File("d:\\leagues.txt"));
		LeagueListResp t = new LeagueListResp();
		 t= t.fromResp(xmlStr);
		System.out.println(t.toString());
	}
}
