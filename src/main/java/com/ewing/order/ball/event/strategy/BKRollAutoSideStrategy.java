package com.ewing.order.ball.event.strategy;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.ball.BetCollector;
import com.ewing.order.ball.bk.bet.BetResp;
import com.ewing.order.ball.bk.bet.BkPreOrderViewResp;
import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.ball.event.BallEvent;
import com.ewing.order.ball.event.BetStrategy;
import com.ewing.order.ball.event.InRateCache;
import com.ewing.order.ball.util.CalUtil;
import com.ewing.order.ball.util.RequestTool;
import com.ewing.order.busi.ball.ddl.BetLog;
import com.ewing.order.busi.ball.ddl.BetRollInfo;
import com.ewing.order.util.BizGenerator;

/**
 * 
 *
 * @author tansonlam
 * @create 2018年8月24日
 */
public class BKRollAutoSideStrategy extends BetStrategy {
	private static Logger log = LoggerFactory.getLogger(BKRollAutoSideStrategy.class);
	private String gtype = "BK";

	/**
	 * 每天下注场数
	 */
	private int MAXEACHDAY = 20;
	
	public static int SYSMAXEACHDAY=0;
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
	 * 全场平均得分率的增加的最大百分比
	 */
	private Float INTERVAL_IN_PERCENT;

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
	/**
	 * 小于多少剩余时间才允许买入
	 */
	private Integer LEFT_TIME = null;

	private DecimalFormat fnum2 = new DecimalFormat("##0.0000");
	/**
	 * 买入方式说明
	 */
	private String buyWayDesc = "";

	private Integer TEST_BUY = 0;

	private BetRollInfo buyRollInfo;
	/**全场比赛得分，1：是   0：前一节得分率*/
	private Integer SCORERATE_ALLMATCH =1;
	@Override
	public void initParam(Map<String, String> paramMap) {
		ALL_AND_QUARTZ_INTERVAL = getFloatParamValue(paramMap, "ALL_AND_QUARTZ_INTERVAL");
		MAX_INTERVAL_PERCENT = getFloatParamValue(paramMap, "MAX_INTERVAL_PERCENT");
		INTERVAL_IN_PERCENT = getFloatParamValue(paramMap, "INTERVAL_IN_PERCENT");
		BUYSIDE = getParamValue(paramMap, "BUYSIDE");
		SQ_NOW = getParamValue(paramMap, "SQ_NOW");
		MONEYEACHMATCH = this.getMoney();
		MIN_HIGH_SCORE_TIME = getIntegerParamValue(paramMap, "MIN_HIGH_SCORE_TIME");
		HIGH_SCORE_COSTTIME = getIntegerParamValue(paramMap, "HIGH_SCORE_COSTTIME");
		EXCLUDE_LEAGUE = getParamValue(paramMap, "EXCLUDE_LEAGUE");
		BUY_WAY = getIntegerParamValue(paramMap, "BUY_WAY");
		LEFT_TIME = getIntegerParamValue(paramMap, "LEFT_TIME");
		if( getIntegerParamValue(paramMap, "TEST_BUY")!=null)
			TEST_BUY = getIntegerParamValue(paramMap, "TEST_BUY");
		if( getIntegerParamValue(paramMap, "SCORERATE_ALLMATCH")!=null)
			SCORERATE_ALLMATCH = getIntegerParamValue(paramMap, "SCORERATE_ALLMATCH");
		if (getIntegerParamValue(paramMap, "MAXEACHDAY") != null)
			MAXEACHDAY = getIntegerParamValue(paramMap, "MAXEACHDAY");
		if(SYSMAXEACHDAY>0)
			MAXEACHDAY = SYSMAXEACHDAY;
		maxInterval = computeMaxInterval();
	}
	private Float computeScoreSec4Alltime(BetRollInfo betRollInfo) {
		return SCORERATE_ALLMATCH ==null || SCORERATE_ALLMATCH == 1  ? InRateCache.computeScoreSec4Alltime(betRollInfo)
				: InRateCache.computeScoreSecBefore4Q(betRollInfo);
	}
	private Float computeMaxInterval() {
		if (ALL_AND_QUARTZ_INTERVAL != null && MAX_INTERVAL_PERCENT != null)
			return ALL_AND_QUARTZ_INTERVAL + MAX_INTERVAL_PERCENT * ALL_AND_QUARTZ_INTERVAL;
		return null;
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
		if (!isNotExcludeLeague(league)) {
			log(gId + "不符号买入规则，排除的联赛");
			return false;
		}
		if (!sw_ou.equals("Y")) {
			log(gId + "不符号买入规则，大小球已经关闭");
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
		List<BetRollInfo> list = BetCollector.CollectDataPool.getRollDetail(gId, 30);
		if (CollectionUtils.isEmpty(list))
			return false;
		BetRollInfo lastBetRollInfo = list.get(list.size() - 1);

		int highScoreTime = 0;
		int tmpHighScoreCostTime = 0;
		BetRollInfo beginBuyRollinfo = null;
		if (CollectionUtils.isEmpty(list))
			return false;
		float inter = 0f;
		BetRollInfo previousBetRollInfo = null;
		String tmpSide = "";
		for (int i = list.size() - 1; i >= 0; i--) {
			
			BetRollInfo betRollInfo = list.get(i);
			Integer tempLeftTime = null;
			if (this.LEFT_TIME != null) {
				tempLeftTime = this.LEFT_TIME;
				if (CalUtil.isNba(betRollInfo.getLeague())) {
					tempLeftTime = this.LEFT_TIME + 100;
				}
			}
			if ((betRollInfo.getSe_now() == null)
					|| (SQ_NOW != null && !SQ_NOW.equals(betRollInfo.getSe_now()))) {
				// log(gId + ",不符合指定场节：" + SQ_NOW + "，当前场节:" +
				// betRollInfo.getSe_now());
				break;
			}
			if (LEFT_TIME != null && Integer.valueOf(betRollInfo.getT_count()) > tempLeftTime) {
				// log(gId + ",大于剩余时间：" + LEFT_TIME + "，当前时间:" +
				// betRollInfo.getT_count());
				break;
			}
			if (previousBetRollInfo != null && previousBetRollInfo.isSameRatioOU(betRollInfo)) {
				continue;
			}
			previousBetRollInfo = betRollInfo;
			float scoreEveryQuartz = InRateCache.computeScoreSec4Quartz(betRollInfo);
			float scoreAllQuartz = computeScoreSec4Alltime(betRollInfo);
			if (scoreEveryQuartz == 0f || scoreAllQuartz == 0f)
				continue;
			inter = Math.abs(scoreEveryQuartz - scoreAllQuartz);
			if (INTERVAL_IN_PERCENT != null)
				ALL_AND_QUARTZ_INTERVAL = scoreAllQuartz * INTERVAL_IN_PERCENT;
			if (isAutoBuy()) {
				if (scoreEveryQuartz > scoreAllQuartz && inter >= ALL_AND_QUARTZ_INTERVAL) {
					tmpSide = "H";
				} else if (scoreEveryQuartz < scoreAllQuartz && inter >= ALL_AND_QUARTZ_INTERVAL) {
					tmpSide = "C";
				} else {
					break;
				}
			}
			// 一旦下一次的买入方改变则跳出循环
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
										- Integer.valueOf(betRollInfo.getT_count()));
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

		}

		if (buyRollInfo != null) {
			float scoreEveryQuartz = InRateCache.computeScoreSec4Quartz(buyRollInfo);
			float scoreAllQuartz =  computeScoreSec4Alltime(buyRollInfo);
			inter = Math.abs(scoreEveryQuartz - scoreAllQuartz);
			String operateName = (BUY_WAY != null && BUY_WAY == 0) ? "反向操作" : "正向操作";
			if (BUY_WAY != null && BUY_WAY == 1) {
				if (side.equals("H")) {
					side = "C";
				} else if (side.equals("C")) {
					side = "H";
				}
			}
			maxInterval = computeMaxInterval();
			if (maxInterval != null && inter >= maxInterval) {
				operateName += (maxInterval != null && inter >= maxInterval)
						? ",再反转大于阀值" + fnum2.format(maxInterval) : "";
				if (side.equals("H")) {
					side = "C";
				} else if (side.equals("C")) {
					side = "H";
				}
			}
			StringBuffer sb = new StringBuffer();
			sb.append("时间:").append(buyRollInfo.getT_count());
			sb.append(",买入方:").append(side.equals("C") ? "大" : "小");
			sb.append(",GID：" + buyRollInfo.getGid());
			sb.append(",滚球开始ID：")
					.append(previousBetRollInfo != null ? previousBetRollInfo.getId() : 0);
			sb.append(",滚球ID：").append(buyRollInfo.getId());
			sb.append(",买入分数:").append(buyRollInfo.getRatio_rou_c());
			sb.append(",总分结果:").append(buyRollInfo.getSc_total());
			sb.append(",Q4-全场得分:").append(fnum2.format(inter));
			sb.append(",出现次数:").append(highScoreTime);
			sb.append(",持续时间").append(tmpHighScoreCostTime);
			sb.append(",买入分率:").append(fnum2.format(scoreEveryQuartz));
			sb.append(",全场分率:").append(fnum2.format(scoreAllQuartz));
			sb.append(",买入方式:").append(operateName);
			buyWayDesc = sb.toString();
			return true;
		}

		// log(gId + ",场节与全场得份率差距：" + inter + "，持续次数:" + highScoreTime);
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

	private Boolean isMatchSpread(BkPreOrderViewResp bkPreOrderViewResp) {
		return buyRollInfo != null
				&& buyRollInfo.getRatio_rou_c().toString().equals(bkPreOrderViewResp.getSpread());
	}

	@Override
	public Object bet(BallEvent ballEvent) {
		String gtype = "BK";// 篮球
		String wtype = "ROU";// 让球
		String betMoney = MONEYEACHMATCH; // 下注金额
		BetResp ftBetResp = null;
		log("===准备下注:" + ballEvent.getSource().toString());
		if (ballEvent.getSource() != null && ballEvent.getSource() instanceof BetInfoDto) {
			BetInfoDto betInfo = (BetInfoDto) ballEvent.getSource();
			try {
				BkPreOrderViewResp bkPreOrderViewResp = getbkPreOrderView(
						this.getBetStrategyContext().getUid(), betInfo.getGid(), gtype, wtype,
						side);
				log("投注前信息：" + bkPreOrderViewResp);
				if(StringUtils.isNotEmpty(bkPreOrderViewResp.getErrormsg())){
					throw new Exception(bkPreOrderViewResp.getErrormsg());
				}
				// 下注前需要再次检查一下次条件
				// if (betCondition(betInfo.getGid(), betInfo.getLeague(),
				// betInfo.getN_sw_OU())) {
				log.info(getStrategyName() + "准备下注:" + ballEvent.getSource().toString()
						+ ",buyWayDesc:" + buyWayDesc + ",betMoney:" + betMoney + ",money:"
						+ this.getMoney());
				if (getBetStrategyContext().isAllowBet() && isAllowBuy()) {
					// if(isMatchSpread(bkPreOrderViewResp)){
					ftBetResp = RequestTool.bkbet(this.getBetStrategyContext().getUid(),
							betInfo.getGid(), gtype, betMoney, wtype, side, bkPreOrderViewResp);
					if (ftBetResp != null){
						ftBetResp.setBuy_desc(buyWayDesc);
						if(!ftBetResp.getCode().equals("560")){
							ftBetResp.setType(side);
							ftBetResp.setSpread(buyRollInfo.getRatio_rou_c().toString());
						}
					}
					/*
					 * }else{ throw new
					 * Exception("错误投注大小分，目标:"+buyRollInfo.getRatio_rou_c()+
					 * ",结果:"+bkPreOrderViewResp.getSpread()); }
					 */
				} else {
					String ioratio = null;
					String spread = null;
					if (bkPreOrderViewResp == null
							|| !StringUtils.isEmpty(bkPreOrderViewResp.getErrormsg())) {
						ioratio = side.equals("C") ? buyRollInfo.getIor_ROUC()
								: buyRollInfo.getIor_ROUH();
						spread = buyRollInfo.getRatio_rou_c().toString();
						buyWayDesc += "，执行历史数据回传率";
					} else {
						ioratio = bkPreOrderViewResp.getIoratio();
						spread = bkPreOrderViewResp.getSpread();
					}
					ftBetResp = BetResp.debugBetResp();
					ftBetResp.setTicket_id(BizGenerator.generateBizNum());
					ftBetResp.setGid(betInfo.getGid());
					ftBetResp.setGold(betMoney);
					ftBetResp.setGtype(gtype);
					ftBetResp.setIoratio(ioratio);
					ftBetResp.setWtype(wtype);
					ftBetResp.setSpread(spread);
					ftBetResp.setType(side);
					ftBetResp.setCode("560"); 
					ftBetResp.setBuy_desc(buyWayDesc);

				}
				// }

			} catch (Exception e) {
				log.error(e.getMessage(), e);
				ftBetResp = BetResp.debugBetResp();
				ftBetResp.setLeague(betInfo.getLeague());
				ftBetResp.setTeam_c(betInfo.getTeam_c());
				ftBetResp.setTeam_h(betInfo.getTeam_h());
				ftBetResp.setTicket_id(BizGenerator.generateBizNum());
				ftBetResp.setGid(betInfo.getGid());
				ftBetResp.setGold(betMoney);
				ftBetResp.setGtype(gtype);
				ftBetResp.setWtype(wtype); 
				ftBetResp.setSpread(betInfo.getRatio_rou_c().toString());
				ftBetResp.setType(side);
				ftBetResp.setCode("500");
				ftBetResp.setBuy_desc(buyWayDesc);
				String errMsg = ExceptionUtils.getMessage(e);
				if (!StringUtils.isEmpty(errMsg)) {
					if (errMsg.length() > 200) {
						errMsg = errMsg.substring(0, 199);
					}
					ftBetResp.setErrormsg(errMsg);
				}
			}
		}
		log.info("===下注结果:" + ftBetResp.toString());
		return ftBetResp;
	}

	public static void main(String[] args) {
		String s = "111111111111";
		System.out.println(s.substring(0, 10000));
	}

}
