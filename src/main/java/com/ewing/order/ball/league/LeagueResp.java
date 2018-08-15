package com.ewing.order.ball.league;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.ewing.order.ball.shared.XMLBean;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 联赛赛程列表
 * 
 * @author tansonlam
 * @create 2018年7月21日
 */
@XStreamAlias("serverresponse")
public class LeagueResp extends XMLBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String systime;
	@XStreamImplicit
	@XStreamAlias("game")
	private List<GameType> game;

	public List<GameType> getGame() {
		return game;
	}

	public void setGame(List<GameType> game) {
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

	public static void main(String[] args) throws IOException {
		String xmlStr = FileUtils.readFileToString(new File("d:\\leagues.txt"));
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(LeagueResp.class);
		LeagueResp t = (LeagueResp) xstream.fromXML(xmlStr);
		System.out.println(t.toString());
	}

}
