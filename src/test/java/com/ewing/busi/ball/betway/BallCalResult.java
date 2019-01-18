package com.ewing.busi.ball.betway;

import java.util.List;
import java.util.Map;

/**
 *
 * @author tansonlam
 * @create 2019年1月16日 
 */
public class BallCalResult {
	private Map<Integer,List<BuyWay>> map;

	public BallCalResult(Map<Integer, List<BuyWay>> map) {
		super();
		this.map = map;
	}

	public Map<Integer, List<BuyWay>> getMap() {
		return map;
	}

	public void setMap(Map<Integer, List<BuyWay>> map) {
		this.map = map;
	}
	
	
	
}
