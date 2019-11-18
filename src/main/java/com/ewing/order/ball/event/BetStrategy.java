package com.ewing.order.ball.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.ball.bk.bet.BetResp;
import com.ewing.order.ball.bk.bet.BkPreOrderViewResp;
import com.ewing.order.ball.logger.BetLogger;
import com.ewing.order.ball.shared.BetLogResult;
import com.ewing.order.ball.shared.BwContinueAllowBet;
import com.ewing.order.ball.shared.BwContinueStatus;
import com.ewing.order.ball.util.CalUtil;
import com.ewing.order.ball.util.RequestTool;
import com.ewing.order.busi.ball.ddl.BetRule;
import com.ewing.order.busi.ball.ddl.BwContinue;
import com.ewing.order.busi.ball.dto.BetDetailDto;

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

	private Integer maxEachday;

	private String winRule;

	private Map<String, String> paramMap;

	private BwContinue bwContinue;

	public String getWinRule() {
		return winRule;
	}

	public void setWinRule(String winRule) {
		this.winRule = winRule;
	}

	public Integer getMaxEachday() {
		return maxEachday;
	}

	public void setMaxEachday(Integer maxEachday) {
		this.maxEachday = maxEachday;
	}

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
		log.info("strategyName:" + strategyName + ",account:" + betStrategyContext.getAccount() + "-" + message);
	}

	public void betlog(String message) {
		BetLogger.logger
				.info("strategyName:" + strategyName + ",account:" + betStrategyContext.getAccount() + "-" + message);
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
	public BkPreOrderViewResp getbkPreOrderView(String uid, String gid, String gtype, String wtype, String side) {
		int retryTime = 0;
		BkPreOrderViewResp bkPreOrderViewResp = null;
		while (retryTime < maxRetryBet) {
			retryTime++;
			try {
				bkPreOrderViewResp = RequestTool.getbkPreOrderView(uid, gid, gtype, wtype, side);
			} catch (Exception e) {
				log("获取投注信息失败,gid:" + gid);
				BetLogger.logger.error("获取投注信息失败,gid:" + gid, e);
			}
			if (bkPreOrderViewResp != null && StringUtils.isEmpty(bkPreOrderViewResp.getErrormsg())) {
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
			Integer loseTotal = loseTotalAfterWinRule();
			bwContinue = this.betStrategyContext.getBwContinueService().newBwContinue(account, ruleId, loseTotal);
		}

		if (StringUtils.isNotEmpty(bwContinue.getContinuePlanMoney())) {
			this.continuePlanMoney = bwContinue.getContinuePlanMoney();
			this.continueMaxMatch = bwContinue.getContinueMaxMatch();
		}
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
		// 固定比例下注
		if (bwContinue.getPoolRate() != null) {
			//支持赢则停的规则
			Boolean allow = allowBetInByWinRule();
			if(!allow){
				return "0";
			}
			totalBetMoney = bwContinue.getRateCurPoolMoney() * bwContinue.getPoolRate();
			return String.valueOf(totalBetMoney.intValue());
		}
		// 连追计划下注
		else if (!StringUtils.isEmpty(this.continuePlanMoney)) {
			String[] planMoneyArray = StringUtils.split(this.continuePlanMoney, ",");
			return planMoneyArray[bwContinue.getTotalMatch()];
		} else {
			// 是否覆盖之前输的金额
			if (this.isCover != null && this.isCover == 1) {
				Float winLossBetMoney = 0f;
				if (bwContinue != null && bwContinue.getTotalBetMoney() != null && bwContinue.getTotalBetMoney() > 0
						&& betRadio > 0) {
					winLossBetMoney = bwContinue.getTotalBetMoney() / betRadio;
				}
				totalBetMoney = winLossBetMoney + betMoney;
				return String.valueOf(totalBetMoney.intValue());
			} else {

				if (bwContinue != null && bwContinue.getTotalMatch() >= bwContinue.getContinueStartLostnum()) {
					totalBetMoney = (bwContinue.getTotalMatch() + 1 - bwContinue.getContinueStartLostnum() + 1)
							* betMoney;
				}
				return String.valueOf(totalBetMoney.intValue());
			}
		}
	}

	/**
	 * 是否不能执行下注
	 * 
	 * @return
	 */
	public Boolean allowBetInByWinRule() {
		if (StringUtils.isEmpty(this.winRule))
			return true;
		String[] winRuleArray = StringUtils.split(this.winRule, ",");
		if (winRuleArray == null || winRuleArray.length != 2)
			return true;
		Integer planWinTotal = Integer.valueOf(winRuleArray[0]);
		Integer planLoseTotal = Integer.valueOf(winRuleArray[1]);
		 
		// 获取所有
		List<BetDetailDto> beforeSucLogList = this.betStrategyContext.getBetLogService()
				.findAllBetDetailBeforeLastestId(betStrategyContext.getAccount(), CalUtil.getStartTimeOfWeek(), null);
		LinkedList<String> resultList = new LinkedList<>();
		for (BetDetailDto betDetailDto : beforeSucLogList) {
			resultList.addLast(betDetailDto.getnResult());
		}
		 
		return allow(planWinTotal, planLoseTotal, resultList); 
	}

	public static Boolean allow(Integer win, Integer lose, List<String> resultList) {
		System.out.println(resultList);
		if (lose == null || lose == 0) {
			return true;
		}
		List<List<String>> allSchemes = new ArrayList<>();
		for (int i = 0; i < lose; i++) {
			List<String> scheme = new ArrayList<>();
			for (int j = 0; j < win; j++) {
				scheme.add(BetLogResult.WIN);
			}
			for (int z = 0; z < i; z++) {
				scheme.add(BetLogResult.LOST);
			}
			allSchemes.add(scheme);
		}
		System.out.println(allSchemes);
		Boolean hitScheme = false;
		for (List<String> scheme : allSchemes) {
			if (hitScheme)
				break;
			System.out.println("方案："+scheme);
			if (resultList.size() < scheme.size()) {
				System.out.println("比赛场数不对称");
				break;
			}
			for (int i = 0; i < scheme.size(); i++) {
				if(resultList.size()-i-1<0 || scheme.size()-i-1<0)
					break; 
				System.out.println("结果," + resultList.get(resultList.size() - i-1) + "," + scheme.get(scheme.size() - i-1));
				if (!resultList.get(resultList.size()-i-1).equals(scheme.get(scheme.size() - i-1))) {
					System.out.println("结果不对称");
					break;
				}

				if (i == 1) {
					hitScheme = true;
				}
			}

		}
		// 命中规则则不能下注
		if (hitScheme) {
			System.out.println("击中方案");
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		String[] result = { BetLogResult.WIN, BetLogResult.LOST, BetLogResult.WIN, BetLogResult.WIN};

		List<String> resultList = Arrays.asList(result);
		Boolean isAllow = allow(3, 1, resultList);
		System.out.println("allow:" + isAllow);
	}

	public Integer loseTotalAfterWinRule() {
		if (StringUtils.isEmpty(this.winRule))
			return 0;
		String[] winRuleArray = StringUtils.split(this.winRule, ",");
		if (winRuleArray == null || winRuleArray.length != 2)
			return 0;
		Integer planWinTotal = Integer.valueOf(winRuleArray[0]);
		Integer planLoseTotal = Integer.valueOf(winRuleArray[1]);
		// 1.计算最后一条成功下注结果是否赢
		List<BetDetailDto> sucBetLogList = this.betStrategyContext.getBetLogService()
				.findSucBetDetail(betStrategyContext.getAccount(), CalUtil.getStartTimeOfWeek(), 1);
		if (CollectionUtils.isEmpty(sucBetLogList))
			return 0;
		Integer lastestBetLogId = null;
		BetDetailDto lastBetLog = sucBetLogList.get(0);

		if (!lastBetLog.getnResult().equals(BetLogResult.WIN))
			return 0;
		lastestBetLogId = lastBetLog.getId();
		// 2.计算赢比赛之前的投注结果，如果有连赢的则会累计起来，和计划赢得场数进行比较
		Integer tempSuc = 1;
		List<BetDetailDto> beforeSucLogList = this.betStrategyContext.getBetLogService()
				.findAllBetDetailBeforeLastestId(betStrategyContext.getAccount(), CalUtil.getStartTimeOfWeek(),
						lastestBetLogId);
		if (CollectionUtils.isNotEmpty(beforeSucLogList)) {
			for (BetDetailDto betDetailDto : beforeSucLogList) {
				if (!betDetailDto.getnResult().equals(BetLogResult.WIN)) {
					break;
				}
				tempSuc++;
			}
		}
		if (tempSuc < planWinTotal) {
			return 0;
		}
		// 3.先计算输的场数
		if (lastestBetLogId != null) {
			List<BetDetailDto> testBetLogList = this.betStrategyContext.getBetLogService()
					.findTestBetDetailAfterLastestId(betStrategyContext.getAccount(), CalUtil.getStartTimeOfWeek(),
							lastestBetLogId);
			if (CollectionUtils.isNotEmpty(testBetLogList)) {
				Integer tempLostNum = 0;
				for (BetDetailDto betDetailDto : testBetLogList) {
					if (!betDetailDto.getnResult().equals(BetLogResult.LOST)) {
						break;
					}

					tempLostNum++;
					// 如果已经输的场数和计划的一样才返回0
					if (tempLostNum == planLoseTotal) {
						return 0;
					}
				}
			}
		}
		return planLoseTotal;
	}

	protected Boolean isAllowBuy() {
		return getIsTest() == null || getIsTest() == 0;
	}
}
