package com.ewing.order.ball.event;

import java.util.Map;

import org.springframework.util.StringUtils;

import com.ewing.order.ball.bk.bet.BetResp;

public abstract class BetStrategy {

	private BetStrategyContext betStrategyContext;

	protected final static Integer maxRetryBet = 3;

	private String strategyName;

	private Integer level;

	protected String uid;

	private String gId;

	private Boolean iseff;

	private Integer ruleId;

	public String getgId() {
		return gId;
	}

	public void setgId(String gId) {
		this.gId = gId;
	}

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public String getUid() {
		return uid;
	}

	public Boolean getIseff() {
		return iseff;
	}

	public void setIseff(Boolean iseff) {
		this.iseff = iseff;
	}

	public void set2Ineff() {
		this.iseff = false;
	}

	/**
	 * 
	 * @param uid
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setBetStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	protected String getParamValue(Map<String, String> paramMap, String key) {
		String value = paramMap.get(key);
		if (StringUtils.isEmpty(value))
			return null;
		return value;
	}

	protected Integer getIntegerParamValue(Map<String, String> paramMap, String key) {
		String value = paramMap.get(key);
		if (StringUtils.isEmpty(value))
			return null;
		return Integer.valueOf(value);
	}

	protected Float getFloatParamValue(Map<String, String> paramMap, String key) {
		String value = paramMap.get(key);
		if (StringUtils.isEmpty(value))
			return null;
		return Float.valueOf(value);
	}

	/**
	 * 初始化
	 */
	public abstract void initParam(Map<String, String> paramMap);

	/**
	 * 策略名称
	 * 
	 * @return
	 */
	public String getStrategyName() {
		return strategyName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * 是否满足投注条件
	 * 
	 * @return
	 */
	public abstract boolean isSatisfy(BallEvent ballEvent);

	public Object betNow(BallEvent ballEvent) {
		int retryTime = 0;
		BetResp ftBetResp = null;
		while (retryTime < maxRetryBet) {
			retryTime++;
			ftBetResp = (BetResp) bet(ballEvent);
			if (ftBetResp != null && !StringUtils.isEmpty(ftBetResp.getTicket_id())) {
				break;
			}
		}

		return ftBetResp;
	}

	/**
	 * 投注
	 * 
	 * @return
	 */
	public abstract Object bet(BallEvent ballEvent);

	public void setBetStrategyContext(BetStrategyContext betStrategyContext) {
		this.betStrategyContext = betStrategyContext;
	}

	public BetStrategyContext getBetStrategyContext() {
		return this.betStrategyContext;
	}
}
