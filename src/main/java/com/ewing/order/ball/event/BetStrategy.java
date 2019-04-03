package com.ewing.order.ball.event;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ewing.order.ball.bk.bet.BetResp;
import com.ewing.order.ball.bk.bet.BkPreOrderViewResp;
import com.ewing.order.ball.logger.BetLogger;
import com.ewing.order.ball.shared.BwContinueAllowBet;
import com.ewing.order.ball.shared.BwContinueStatus;
import com.ewing.order.ball.util.RequestTool;
import com.ewing.order.busi.ball.ddl.BetRule;
import com.ewing.order.busi.ball.ddl.BwContinue;

public abstract class BetStrategy {
	private static Logger log = LoggerFactory.getLogger(BetStrategy.class);
	private BetStrategyContext betStrategyContext;

	protected final static Integer maxRetryBet = 10;

	private String strategyName;

	private Integer level;

	protected String uid;

	private String gId;

	private Boolean iseff;

	private Integer ruleId;

	private String money;

	private Integer continueMaxMatch;
	
	private String continuePlanMoney;

	private Integer isTest;

	private Integer isCover;

	private Map<String, String> paramMap;

	private BwContinue bwContinue;

	public String getContinuePlanMoney() {
		return continuePlanMoney;
	}

	public void setContinuePlanMoney(String continuePlanMoney) {
		this.continuePlanMoney = continuePlanMoney;
	}

	public Integer getIsCover() {
		return isCover;
	}

	public void setIsCover(Integer isCover) {
		this.isCover = isCover;
	}

	public BwContinue getBwContinue() {
		return bwContinue;
	}

	public void setBwContinue(BwContinue bwContinue) {
		this.bwContinue = bwContinue;
	}

	public Integer getIsTest() {
		return isTest;
	}

	public void setIsTest(Integer isTest) {
		this.isTest = isTest;
	}

	public Integer getContinueMaxMatch() {
		return continueMaxMatch;
	}

	public void setContinueMaxMatch(Integer continueMaxMatch) {
		this.continueMaxMatch = continueMaxMatch;
	}

	public Map<String, String> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

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

	public void log(String message) {
		log.info("strategyName:" + strategyName + ",account:" + betStrategyContext.getAccount()
				+ "-" + message);
	}

	public void betlog(String message) {
		BetLogger.logger.info("strategyName:" + strategyName + ",account:"
				+ betStrategyContext.getAccount() + "-" + message);
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
	 * 描述
	 */
	public String desc(BetRule betRule) {
		return "";
	}

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
	 * 获取投注信息
	 * 
	 * @param uid
	 * @param gid
	 * @param gtype
	 * @param wtype
	 * @param side
	 * @return
	 */
	public BkPreOrderViewResp getbkPreOrderView(String uid, String gid, String gtype, String wtype,
			String side) {
		int retryTime = 0;
		BkPreOrderViewResp bkPreOrderViewResp = null;
		while (retryTime < maxRetryBet) {
			retryTime++;
			try {
				bkPreOrderViewResp = RequestTool.getbkPreOrderView(uid, gid, gtype, wtype, side);
			} catch (Exception e) {
				log("获取投注信息失败,gid:" + gid);
			}
			if (bkPreOrderViewResp != null
					&& StringUtils.isEmpty(bkPreOrderViewResp.getErrormsg())) {
				break;
			}
		}

		return bkPreOrderViewResp;
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

	protected Boolean allowBuyInBwContinue(String account) {
		if (this.continueMaxMatch == null || this.continueMaxMatch == 0) {
			return true;
		}
		bwContinue = this.betStrategyContext.getBwContinueService().findRunning(account, ruleId);
		if (bwContinue == null) {
			return true;
		}
		if (bwContinue.getAllowBet().equals(BwContinueAllowBet.NOTALLOW)) {
			return false;
		}
		if (!bwContinue.getStatus().equals(BwContinueStatus.RUNNING)) {
			return false;
		}
		if (bwContinue.getTotalMatch() >= this.continueMaxMatch) {
			return false;
		}
		return true;
	}

	protected String computeBetMoney(Float betRadio, Float betMoney) {
		if (this.continueMaxMatch == null || this.continueMaxMatch == 0) {
			return String.valueOf(betMoney.intValue());
		}

		Float totalBetMoney = betMoney;
		if(!StringUtils.isEmpty(this.continuePlanMoney)){
			String[] planMoneyArray = StringUtils.split(this.continuePlanMoney, ",");
			return planMoneyArray[bwContinue.getTotalMatch()];
		}else{
			//是否覆盖之前输的金额
			if (this.isCover != null && this.isCover == 1) {
				Float winLossBetMoney = 0f;
				if (bwContinue != null && bwContinue.getTotalBetMoney() != null
						&& bwContinue.getTotalBetMoney() > 0 && betRadio > 0) {
					winLossBetMoney = bwContinue.getTotalBetMoney() / betRadio;
				}
				totalBetMoney = winLossBetMoney + betMoney;
				return String.valueOf(totalBetMoney.intValue());
			} else {
	
				if (bwContinue != null
						&& bwContinue.getTotalMatch() >= bwContinue.getContinueStartLostnum()) {
					totalBetMoney = (bwContinue.getTotalMatch() + 1
							- bwContinue.getContinueStartLostnum() + 1) * betMoney;
				}
				return String.valueOf(totalBetMoney.intValue());
			}
		}
	}

	protected Boolean isAllowBuy() {
		return getIsTest() == null || getIsTest() == 0;
	}
}
