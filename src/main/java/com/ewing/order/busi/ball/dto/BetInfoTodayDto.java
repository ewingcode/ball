package com.ewing.order.busi.ball.dto;

/**
 *
 * @author tansonlam
 * @create 2019年8月8日 
 */
public class BetInfoTodayDto {
	/**
	 * 今日滚球赛事数量
	 */
	private Integer rollCount;
	/**
	 * 最近更新滚球赛事时间
	 */
	private String lastUpdateRollCount;
	
	/**
	 * 今日赛事数量
	 */
	private Integer todayCount;
	/**
	 * 最近更新今日赛事时间
	 */
	private String lastUpdateTodayCount;

	public String getLastUpdateRollCount() {
		return lastUpdateRollCount;
	}

	public void setLastUpdateRollCount(String lastUpdateRollCount) {
		this.lastUpdateRollCount = lastUpdateRollCount;
	}

	public String getLastUpdateTodayCount() {
		return lastUpdateTodayCount;
	}

	public void setLastUpdateTodayCount(String lastUpdateTodayCount) {
		this.lastUpdateTodayCount = lastUpdateTodayCount;
	}

	public Integer getRollCount() {
		return rollCount;
	}

	public void setRollCount(Integer rollCount) {
		this.rollCount = rollCount;
	}

	public Integer getTodayCount() {
		return todayCount;
	}

	public void setTodayCount(Integer todayCount) {
		this.todayCount = todayCount;
	}
	
	
}
