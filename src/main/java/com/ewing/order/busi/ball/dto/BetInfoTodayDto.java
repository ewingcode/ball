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
	 * 今日赛事数量
	 */
	private Integer todayCount;

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
