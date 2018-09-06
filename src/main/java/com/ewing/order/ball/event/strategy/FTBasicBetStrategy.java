package com.ewing.order.ball.event.strategy;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ewing.order.ball.bk.bet.BkPreOrderViewResp;
import com.ewing.order.ball.event.BallEvent;
import com.ewing.order.ball.event.BetStrategy;
import com.ewing.order.ball.bk.bet.BetResp;
import com.ewing.order.ball.ft.game.FtGame;
import com.ewing.order.ball.util.RequestTool;
import com.ewing.order.busi.ball.ddl.BetLog;
import com.ewing.order.util.ScriptUtil;

/**
 * 今日足球投注的基础策略，只买入大小球
 *
 * @author tansonlam
 * @create 2018年7月31日
 */
public class FTBasicBetStrategy extends BetStrategy {
	private static Logger log = LoggerFactory.getLogger(FTBasicBetStrategy.class);
	private String gtype = "FT";
 
	/**
	 * 每天下注场数
	 */
	private int MAXEACHDAY = 5;
	/**
	 * 每场比赛下注次数
	 */
	private int MAXEACHMATCH = 1;
	/**
	 * 买大小球， 球数
	 */
	private int BALL = 3;
	/**
	 * 买大小球，比较符号
	 */
	private String BALL_COMPARE = ">=";
	/**
	 * 买大减去小球，水位差异
	 */
	private float MINROUDIS = 0.2f;

	private String MINROUDIS_COMPARE = ">=";
	/**
	 * 每次买入金额
	 */
	private String MONEYEACHMATCH = "50";

	/**
	 * 买大小方 ,H是大，C是小
	 */
	private String BUYSIDE = "H";
	
	private String specGameId = "3318562";

	@Override
	public void initParam(Map<String, String> paramMap) {
		MAXEACHDAY = Integer.valueOf(getParamValue(paramMap, "MAXEACHDAY"));
		MAXEACHMATCH = Integer.valueOf(getParamValue(paramMap, "MAXEACHMATCH"));
		BALL = Integer.valueOf(getParamValue(paramMap, "BALL"));
		BALL_COMPARE = getParamValue(paramMap, "BALL_COMPARE");
		MINROUDIS = Float.valueOf(getParamValue(paramMap, "MINROUDIS"));
		MINROUDIS_COMPARE = getParamValue(paramMap, "MINROUDIS_COMPARE");
		MONEYEACHMATCH = getParamValue(paramMap, "MONEYEACHMATCH");
		BUYSIDE = getParamValue(paramMap, "BUYSIDE");
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
	
		boolean ret = false;
		if (ballEvent.getSource() != null && ballEvent.getSource() instanceof FtGame) {
			FtGame ftGame = (FtGame) ballEvent.getSource();
			if(!StringUtils.isEmpty(specGameId) && !ftGame.getGid().equals(specGameId)){
				return false;
			}
			// 是否有大小球投注，并且排除了角球
			ret = betCondition(ftGame.getGid(), ftGame.getSw_OU(), ftGame.getPtype(),
					ftGame.getRatio_u(), ftGame.getIor_OUH(), ftGame.getIor_OUC());
		}
		return ret;
	}

	private boolean betCondition(String gId, String sw_ou, String ptype, String radio_u,
			String ior_OUH, String ior_OUC) {
		if (sw_ou.equals("Y") && StringUtils.isEmpty(ptype) && fixRadioOU(radio_u)
				&& lowerIor(Float.valueOf(ior_OUH), Float.valueOf(ior_OUC))
				&& betTimeEachMatch(this.getBetStrategyContext().getAccount(), gId) < MAXEACHMATCH
				&& betTimeEachDay(this.getBetStrategyContext().getAccount()) < MAXEACHDAY) {
			return true;
		}
		return false;
	}

	/**
	 * 合符要求的大小球数
	 * 
	 * @param radio
	 * @return
	 */
	private Boolean fixRadioOU(String radio) {
		String minRadio = "";
		if (radio.indexOf("/") > -1)
			minRadio = radio.substring(0, radio.indexOf("/")).replace("小", "").replace(" ", "");
		else
			minRadio = radio.replace("小", "").replace(" ", "");
		return ScriptUtil.eval(Float.valueOf(minRadio) + BALL_COMPARE + BALL);
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

	/**
	 * 较低的赔率,大比小的赔率高于0.2
	 * 
	 * @param ior_OUH
	 * @param ior_OUC
	 * @return
	 */
	private Boolean lowerIor(Float ior_OUH, Float ior_OUC) {
		return ScriptUtil.eval((ior_OUC - ior_OUH) + MINROUDIS_COMPARE + MINROUDIS);
	}

	@Override
	public Object bet(BallEvent ballEvent) {
		String gtype = "FT";// 足球
		String wtype = "OU";// 大小球
		String betMoney = MONEYEACHMATCH; // 下注金额300
		String side = BUYSIDE; // 买小
		BetResp ftBetResp = null;

		if (ballEvent.getSource() != null && ballEvent.getSource() instanceof FtGame) {
			FtGame game = (FtGame) ballEvent.getSource();
			try {
				BkPreOrderViewResp bkPreOrderViewResp = RequestTool.getFtPreOrderView(uid,
						game.getGid(), gtype, wtype, side);
				if (betCondition(game.getGid(), game.getSw_OU(), game.getPtype(), game.getRatio_u(),
						game.getIor_OUH(), game.getIor_OUC())) {
					log.info(getStrategyName() + "准备下注:" + ballEvent.getSource().toString());
					if (getBetStrategyContext().isAllowBet()) {
						ftBetResp = RequestTool.ftbet(uid, game.getGid(), gtype, betMoney, wtype,
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
