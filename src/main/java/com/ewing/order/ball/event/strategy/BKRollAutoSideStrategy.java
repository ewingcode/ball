package com.ewing.order.ball.event.strategy;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ewing.order.ball.BetCollector;
import com.ewing.order.ball.bk.bet.BetResp;
import com.ewing.order.ball.bk.bet.BkPreOrderViewResp;
import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.ball.event.BallEvent;
import com.ewing.order.ball.event.BetStrategy;
import com.ewing.order.ball.util.CalUtil;
import com.ewing.order.ball.util.RequestTool;
import com.ewing.order.busi.ball.ddl.BetLog;
import com.ewing.order.busi.ball.ddl.BetRollInfo;
import com.ewing.order.util.BizGenerator;
import com.google.common.collect.Lists;

/**
 * 
 *
 * @author tansonlam
 * @create 2018年8月24日
 */
public class BKRollAutoSideStrategy extends BetStrategy {
	private static Logger log = LoggerFactory.getLogger(BKRollAutoSideStrategy.class);
	private String gtype = "BK";
	private String uid;
	/**
	 * 每天下注场数
	 */
	private int MAXEACHDAY = 10;
	/**
	 * 每场比赛下注次数
	 */
	private int MAXEACHMATCH = 1;
	/**
	 * 每次买入金额
	 */
	private String MONEYEACHMATCH = "50";

	/**
	 * 全场平均得分率和某节得分率差值
	 */
	private Float ALL_AND_QUARTZ_INTERVAL;

	/**
	 * 买入方
	 */
	private String BUYSIDE;
	/**
	 * 指定场节
	 */
	private String SQ_NOW;
	/**
	 * 连续较高入球数的出现次数
	 */
	private Integer MIN_HIGH_SCORE_TIME;
	/**
	 * 全场平均得分率和某节得分率差值增加最大百分比
	 */
	private Float MAX_INTERVAL_PERCENT;

	/**
	 * 买入方式 0:反向 1:正向
	 */
	private Integer BUY_WAY;

	/**
	 * 全场平均得分率和某节得分率差值持续时间
	 */
	private Integer HIGH_SCORE_COSTTIME;

	private String EXCLUDE_LEAGUE = "";
	/**
	 * 实际买入方
	 */
	private String side;
	/**
	 * 最大得分率
	 */
	private Float maxInterval = null;

	private DecimalFormat fnum2 = new DecimalFormat("##0.0000");
	/**
	 * 买入方式说明
	 */
	private String buyWayDesc = "";
	 
	@Override
	public void initParam(Map<String, String> paramMap) {
		ALL_AND_QUARTZ_INTERVAL = getFloatParamValue(paramMap, "ALL_AND_QUARTZ_INTERVAL");
		MAX_INTERVAL_PERCENT = getFloatParamValue(paramMap, "MAX_INTERVAL_PERCENT");
		BUYSIDE = getParamValue(paramMap, "BUYSIDE");
		SQ_NOW = getParamValue(paramMap, "SQ_NOW");
		MONEYEACHMATCH = getParamValue(paramMap, "MONEYEACHMATCH");
		MIN_HIGH_SCORE_TIME = getIntegerParamValue(paramMap, "MIN_HIGH_SCORE_TIME");
		HIGH_SCORE_COSTTIME = getIntegerParamValue(paramMap, "HIGH_SCORE_COSTTIME");
		EXCLUDE_LEAGUE = getParamValue(paramMap, "EXCLUDE_LEAGUE");
		BUY_WAY = getIntegerParamValue(paramMap, "BUY_WAY");
		if (ALL_AND_QUARTZ_INTERVAL != null && MAX_INTERVAL_PERCENT != null)
			maxInterval = ALL_AND_QUARTZ_INTERVAL + MAX_INTERVAL_PERCENT * ALL_AND_QUARTZ_INTERVAL;

	}
   
	/**
	 * 
	 * @param uid
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	public boolean isSatisfy(BallEvent ballEvent) {
		if (ballEvent.getSource() != null && ballEvent.getSource() instanceof BetInfoDto) {
			BetInfoDto bkGame = (BetInfoDto) ballEvent.getSource();
			// 是否有滚球大小球的投注
			if (betCondition(bkGame.getGid(), bkGame.getLeague(), bkGame.getN_sw_OU())) {
				return true;
			}
		}
		return false;
	}

	private boolean isNotExcludeLeague(String league) {
		if (StringUtils.isEmpty(EXCLUDE_LEAGUE))
			return true;
		String[] str = StringUtils.split(EXCLUDE_LEAGUE, ",");
		for (String s : str) {
			if (league.indexOf(s) > -1) {
				return false;
			}
		}
		return true;
	}

	private boolean betCondition(String gId, String league, String sw_ou) {
		if (!sw_ou.equals("Y")) {
			log(gId + "不符号买入规则，大小球已经关闭");
			return false;
		}
		if (!isNotExcludeLeague(league)) {
			log(gId + "不符号买入规则，排除的联赛");
			return false;
		}
		if (!fixHighScore(gId)) {
			log(gId + "不符合全场与某节的差值,差值：" + ALL_AND_QUARTZ_INTERVAL + ",出现次数:" + MIN_HIGH_SCORE_TIME);
			return false;
		}
		if (!(betTimeEachMatch(this.getBetStrategyContext().getAccount(), gId) < MAXEACHMATCH)) {
			log(gId + "不符号买入规则，本次比赛最大买入" + MAXEACHMATCH + "次");
			return false;
		}
		if (!(betTimeEachDay(this.getBetStrategyContext().getAccount()) < MAXEACHDAY)) {
			log(gId + "不符合高分规则,每天最大买入" + MAXEACHDAY + "场");
			return false;
		}
		return true;
	}

	private Boolean isAutoBuy() {
		return BUYSIDE.equals("AUTO");
	}

	public boolean fixHighScore(String gId) {
		side = "";
		List<BetRollInfo> list = BetCollector.CollectDataPool.getRollDetail(gId,
				30);
		if(CollectionUtils.isEmpty(list))
			return false;
		BetRollInfo lastBetRollInfo = list.get(list.size()-1);
		int highScoreTime = 0;
		int tmpHighScoreCostTime = 0;
		BetRollInfo beginBuyRollinfo = null;
		BetRollInfo buyRollInfo = null;
		if (CollectionUtils.isEmpty(list))
			return false;
		float inter = 0f;
		BetRollInfo previousBetRollInfo = null;
		String tmpSide = "";
		for (int i = list.size() - 1; i >= 0; i--) {
			BetRollInfo betRollInfo = list.get(i);
			if ((betRollInfo.getSe_now() == null)
					|| (SQ_NOW != null && !SQ_NOW.equals(betRollInfo.getSe_now()))) {
				log(gId + ",不符合指定场节：" + SQ_NOW + "，当前:" + betRollInfo.getSe_now());
				break;
			}
			if (previousBetRollInfo != null && previousBetRollInfo.isSameRatioOU(betRollInfo)) {
				continue;
			}

			float scoreEveryQuartz = CalUtil.computeScoreSec4Quartz(betRollInfo);
			float scoreAllQuartz = CalUtil.computeScoreSec4Alltime(betRollInfo);
			if (scoreEveryQuartz == 0f || scoreAllQuartz == 0f)
				continue;
			inter = Math.abs(scoreEveryQuartz - scoreAllQuartz);
			if (isAutoBuy()) {
				if (scoreEveryQuartz > scoreAllQuartz && inter >= ALL_AND_QUARTZ_INTERVAL) {
					tmpSide = "H";
				} else if (scoreEveryQuartz < scoreAllQuartz && inter >= ALL_AND_QUARTZ_INTERVAL) {
					tmpSide = "C";
				} else { 
					break;
				}
			}
			//一旦下一次的买入方改变则跳出循环
			if (!StringUtils.isEmpty(tmpSide) && !StringUtils.isEmpty(side)
					&& !tmpSide.equals(side)) {
				break;
			}
			if (!StringUtils.isEmpty(tmpSide)) {
				if (!tmpSide.equals(side)) {
					highScoreTime = 0;
				}
				side = tmpSide;
			}

			if (side.equalsIgnoreCase("H")) {
				if (scoreEveryQuartz - scoreAllQuartz >= ALL_AND_QUARTZ_INTERVAL) {
					highScoreTime++;
					if (beginBuyRollinfo == null)
						beginBuyRollinfo = betRollInfo;
					else
						tmpHighScoreCostTime = Math
								.abs(Integer.valueOf(beginBuyRollinfo.getT_count())
										- Integer.valueOf(betRollInfo.getT_count()));
								 
				} else {
					highScoreTime = 0;
					tmpHighScoreCostTime = 0;
					beginBuyRollinfo = null;
				}
			} else if (side.equalsIgnoreCase("C")) {
				if (scoreAllQuartz - scoreEveryQuartz >= ALL_AND_QUARTZ_INTERVAL) {
					highScoreTime++;
					if (beginBuyRollinfo == null)
						beginBuyRollinfo = betRollInfo;
					else
						tmpHighScoreCostTime = Math
								.abs(Integer.valueOf(beginBuyRollinfo.getT_count())
										- Integer.valueOf(betRollInfo.getT_count())) ;
				} else {
					highScoreTime = 0;
					beginBuyRollinfo = null;
					tmpHighScoreCostTime = 0;
				}
			}
			if (MIN_HIGH_SCORE_TIME != null && HIGH_SCORE_COSTTIME != null) {
				if (highScoreTime >= MIN_HIGH_SCORE_TIME
						&& tmpHighScoreCostTime >= HIGH_SCORE_COSTTIME) {
					buyRollInfo = lastBetRollInfo;
					break;
				}
			} else {

				if (MIN_HIGH_SCORE_TIME != null && highScoreTime == MIN_HIGH_SCORE_TIME) {
					buyRollInfo = lastBetRollInfo;
					break;
				}

				if (HIGH_SCORE_COSTTIME != null && tmpHighScoreCostTime >= HIGH_SCORE_COSTTIME) {
					buyRollInfo = lastBetRollInfo;
					break;
				}

			}
			previousBetRollInfo = betRollInfo;

		}
		String operateName = (BUY_WAY != null && BUY_WAY == 0) ? "反向操作" : "正向操作";
		if ((BUY_WAY != null && BUY_WAY == 1) || (maxInterval != null && inter >= maxInterval)) {
			operateName += (maxInterval != null && inter >= maxInterval)
					? ",再反转大于阀值" + fnum2.format(maxInterval) : "";
			if (side.equals("H")) {
				side = "C";
			} else if (side.equals("C")) {
				side = "H";
			}
		}

		if (buyRollInfo != null) { 
			StringBuffer sb = new StringBuffer();
			sb.append("滚球开始ID：").append(previousBetRollInfo!=null?previousBetRollInfo.getId():0);
			sb.append("滚球ID：").append(buyRollInfo.getId());
			sb.append(",买入分率:").append(fnum2.format(CalUtil.computeScoreSec4Quartz(buyRollInfo)));
			sb.append(",全场分率:").append(fnum2.format(CalUtil.computeScoreSec4Quartz(buyRollInfo)));
			sb.append(",买入方式:").append(operateName);
			buyWayDesc = sb.toString();
			return true;
		}

		log(gId + ",场节与全场得份率差距：" + inter + "，持续次数:" + highScoreTime);
		return false;
	}

	/**
	 * 每场比赛的下注次数
	 * 
	 * @param account
	 * @param gId
	 * @return
	 */
	private int betTimeEachMatch(String account, String gId) {
		List<BetLog> histories = this.getBetStrategyContext().getBetLogService().findSucBet(account,
				gId);
		return CollectionUtils.isEmpty(histories) ? 0 : histories.size();
	}

	/**
	 * 每天的下注次数
	 * 
	 * @return
	 */
	private int betTimeEachDay(String account) {
		List<BetLog> histories = this.getBetStrategyContext().getBetLogService()
				.findEachDay(account, gtype);
		return CollectionUtils.isEmpty(histories) ? 0 : histories.size();
	}

	@Override
	public Object bet(BallEvent ballEvent) {
		String gtype = "BK";// 篮球
		String wtype = "ROU";// 让球
		String betMoney = MONEYEACHMATCH; // 下注金额
		BetResp ftBetResp = null;
		log.info("===准备下注:" + ballEvent.getSource().toString());
		if (ballEvent.getSource() != null && ballEvent.getSource() instanceof BetInfoDto) {
			BetInfoDto betInfo = (BetInfoDto) ballEvent.getSource();
			try {
				BkPreOrderViewResp bkPreOrderViewResp = RequestTool.getbkPreOrderView(uid,
						betInfo.getGid(), gtype, wtype, side);
				log.info("投注前信息：" + bkPreOrderViewResp);
				// 下注前需要再次检查一下次条件
				if (betCondition(betInfo.getGid(), betInfo.getLeague(), betInfo.getN_sw_OU())) {
					log.info(getStrategyName() + "准备下注:" + ballEvent.getSource().toString()+",buyWayDesc:"+buyWayDesc);
					if (getBetStrategyContext().isAllowBet()) {
						ftBetResp = RequestTool.bkbet(uid, betInfo.getGid(), gtype, betMoney, wtype,
								side, bkPreOrderViewResp);
						if(ftBetResp!=null){
							ftBetResp.setBuy_way(buyWayDesc);
						}
					} else {
						ftBetResp = BetResp.debugBetResp();
						ftBetResp.setTicket_id(BizGenerator.generateBizNum());
						ftBetResp.setGid(betInfo.getGid());
						ftBetResp.setGold(betMoney);
						ftBetResp.setGtype(gtype);
						ftBetResp.setIoratio(bkPreOrderViewResp.getIoratio());
						ftBetResp.setWtype(wtype);
						ftBetResp.setSpread(bkPreOrderViewResp.getSpread());
						ftBetResp.setType(side);
						ftBetResp.setCode("560");
						ftBetResp.setBuy_way(buyWayDesc);
					}
				}

			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		log.info("===下注结果:" + ftBetResp.toString());
		return ftBetResp;
	}

}
