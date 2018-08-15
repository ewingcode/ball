package com.ewing.order.ball.event;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.ball.RequestTool;
import com.ewing.order.ball.bk.bet.BkPreOrderViewResp;
import com.ewing.order.ball.bk.bet.BetResp;
import com.ewing.order.ball.bk.game.BkGame;
import com.ewing.order.ball.ft.game.FtGame;
import com.ewing.order.busi.ball.ddl.BetLog;

/**
 * 今日篮球投注基础策略，只买入让分
 *
 * @author tansonlam
 * @create 2018年7月31日
 */
public class BKBasicBetStrategy extends BetStrategy {
	private static Logger log = LoggerFactory.getLogger(BKBasicBetStrategy.class);
	private String gtype = "BK";
	private String uid;
	/**
	 * 每天下注场数
	 */
	private int MAXEACHDAY = 3;
	/**
	 * 每场比赛下注次数
	 */
	private int MAXEACHMATCH = 0;
	/**
	 * 买让球，最小的让球数
	 */
	private int MINRADIOR = 30;
	/**
	 * 每次买入金额
	 */
	private String MONEYEACHMATCH = "50";

	/**
	 * 
	 * @param uid
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	public String gameId() {
		return null;
	}

	@Override
	public boolean isSatisfy(BallEvent ballEvent) {
		if (ballEvent.getSource() != null && ballEvent.getSource() instanceof BkGame) {
			BkGame bkGame = (BkGame) ballEvent.getSource();
			return betCondition(bkGame.getGid(), bkGame.getSw_R(), bkGame.getRatio());
		}
		return false;
	}

	private boolean betCondition(String gId, String sw_R, String radio) {
		// 是否有让球的投注
		if (sw_R.equals("Y") && fixRadio(radio)
				&& betTimeEachMatch(this.getBetStrategyContext().getAccount(), gId) <= MAXEACHMATCH
				&& betTimeEachDay(this.getBetStrategyContext().getAccount()) <= MAXEACHDAY) {
			return true;
		}
		return false;
	}

	/**
	 * 合符要求的让球
	 * 
	 * @param radio
	 * @return
	 */
	private Boolean fixRadio(String radio) {
		return Float.valueOf(radio) > MINRADIOR;
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
		String wtype = "R";// 让球
		String betMoney = MONEYEACHMATCH; // 下注金额
		String side = ""; // 买小
		BetResp ftBetResp = null;
		log.info("===准备下注:" + ballEvent.getSource().toString());
		if (ballEvent.getSource() != null && ballEvent.getSource() instanceof FtGame) {
			BkGame game = (BkGame) ballEvent.getSource();
			try {
				side = game.getStrong().equals("H") ? "C" : "H";
				BkPreOrderViewResp bkPreOrderViewResp = RequestTool.getbkPreOrderView(uid,
						game.getGid(), gtype, wtype, side);
				log.info("投注前信息：" + bkPreOrderViewResp);
				// 下注前需要再次检查一下次条件
				if (betCondition(game.getGid(), "Y", bkPreOrderViewResp.getSpread())) {
					ftBetResp = RequestTool.bkbet(uid, game.getGid(), gtype, betMoney, wtype, side,
							bkPreOrderViewResp);
				}

			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		log.info("===下注结果:" + ftBetResp.toString());
		return ftBetResp;
	}

	@Override
	public void initParam(Map<String, String> paramMap) {
		// TODO Auto-generated method stub

	}

}
