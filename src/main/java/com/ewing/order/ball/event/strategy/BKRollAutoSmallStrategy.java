package com.ewing.order.ball.event.strategy;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ewing.order.ball.BetCollector;
import com.ewing.order.ball.RequestTool;
import com.ewing.order.ball.bk.bet.BetResp;
import com.ewing.order.ball.bk.bet.BkPreOrderViewResp;
import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.ball.event.BallEvent;
import com.ewing.order.ball.event.BetStrategy;
import com.ewing.order.busi.ball.ddl.BetLog;
import com.ewing.order.busi.ball.ddl.BetRollInfo;
import com.ewing.order.common.exception.BusiException;

/**
 *  
 *
 * @author tansonlam
 * @create 2018年8月24日
 */
public class BKRollAutoSmallStrategy extends BetStrategy {
	private static Logger log = LoggerFactory.getLogger(BKRollAutoSmallStrategy.class);
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
	 * 设置每秒的较高入球数
	 */
	private float HIGH_SCORE_EACHSEC = 0.1f;
	/**
	 * 连续较高入球数的出现次数
	 */
	private int MIN_TIME_HIGH_SCORE_EACHSEC = 3;
	
	private String EXCLUDE_LEAGUE="";

	@Override
	public void initParam(Map<String, String> paramMap) {
		HIGH_SCORE_EACHSEC = getFloatParamValue(paramMap, "HIGH_SCORE_EACHSEC");
		MONEYEACHMATCH = getParamValue(paramMap, "MONEYEACHMATCH");
		MIN_TIME_HIGH_SCORE_EACHSEC = getIntegerParamValue(paramMap, "MIN_TIME_HIGH_SCORE_EACHSEC");
		EXCLUDE_LEAGUE =  getParamValue(paramMap, "EXCLUDE_LEAGUE");
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
			if (betCondition(bkGame.getGid(), bkGame.getLeague(),bkGame.getN_sw_OU())) {
				return true;
			}
		}
		return false;
	}

	private boolean isNotExcludeLeague(String league){
		if(StringUtils.isEmpty(EXCLUDE_LEAGUE))
			return true;
		String[] str = StringUtils.split(EXCLUDE_LEAGUE, ",");
		for(String s : str){
			if(league.indexOf(s)>-1){
				return false;
			}
		}
		return true;
	}
	
	private boolean betCondition(String gId,String league, String sw_ou) { 
		if (!sw_ou.equals("Y")) {
			log(gId+"不符号买入规则，大小球已经关闭");
			return false;
		}
		if(!isNotExcludeLeague(league)){
			log(gId+"不符号买入规则，排除的联赛");
			return false;
		}
		if(!fixHighScore(gId)){
			log(gId+"不符合高分规则,每秒："+HIGH_SCORE_EACHSEC+",出现次数:"+MIN_TIME_HIGH_SCORE_EACHSEC);
			return false;
		}
		if(!(betTimeEachMatch(this.getBetStrategyContext().getAccount(), gId) < MAXEACHMATCH)){
			log(gId+"不符号买入规则，本次比赛最大买入"+MAXEACHMATCH+"次");
			return false;
		}
		if(!(betTimeEachDay(this.getBetStrategyContext().getAccount()) < MAXEACHDAY)){
			log(gId+"不符合高分规则,每天最大买入"+MAXEACHDAY+"场");
			return false;
		} 
		return true;
	}

	private Float computeScoreSec(BetRollInfo betRollInfo) {
		Integer costTime = 600 - Integer.valueOf(betRollInfo.getT_count());
		if (betRollInfo.getSe_now().equals("Q1")) {
			return Float.valueOf(betRollInfo.getSc_Q1_total()) / costTime;
		} else if (betRollInfo.getSe_now().equals("Q2")) {
			return Float.valueOf(betRollInfo.getSc_Q2_total()) / costTime;
		} else if (betRollInfo.getSe_now().equals("Q3")) {
			return Float.valueOf(betRollInfo.getSc_Q3_total()) / costTime;
		} else if (betRollInfo.getSe_now().equals("Q4")) {
			return Float.valueOf(betRollInfo.getSc_Q4_total()) / costTime;
		}
		return 0f;
	}

	public boolean fixHighScore(String gid) {
		List<BetRollInfo> list = BetCollector.CollectDataPool.getRollDetail(gid,
				MIN_TIME_HIGH_SCORE_EACHSEC);
		int highScoreTIme = 0;
		if(CollectionUtils.isEmpty(list))
			return false;
		for (BetRollInfo betRollInfo : list) {

			float scoreEveryQuartz = computeScoreSec(betRollInfo);

			if (scoreEveryQuartz >= HIGH_SCORE_EACHSEC) {
				highScoreTIme++;
			} else {
				highScoreTIme = 0;
			}
			if (highScoreTIme == MIN_TIME_HIGH_SCORE_EACHSEC) {
				return true;
			}

		}
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
		String side = "H"; // 买小
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
					log.info(getStrategyName() + "准备下注:" + ballEvent.getSource().toString());
					if (getBetStrategyContext().isAllowBet()) {
						ftBetResp = RequestTool.bkbet(uid, betInfo.getGid(), gtype, betMoney, wtype,
								side, bkPreOrderViewResp);
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
