package com.ewing.order.ball.event.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.ball.RequestTool;
import com.ewing.order.ball.bk.bet.BetResp;
import com.ewing.order.ball.bk.bet.BkPreOrderViewResp;
import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.ball.event.BallEvent;
import com.ewing.order.ball.event.BetStrategy;
import com.ewing.order.busi.ball.ddl.BetLog;
import com.ewing.order.busi.ball.ddl.BetRule;
import com.ewing.order.common.exception.BusiException;
import com.ewing.order.util.GsonUtil;
import com.ewing.order.util.ScriptUtil;

public class BKRollSetReStrategy extends BetStrategy {
	private static Logger log = LoggerFactory.getLogger(BKRollAutoSmallStrategy.class);

	private String uid;

	/**
	 * 每场比赛下注次数
	 */
	private int MAXEACHMATCH = 0;
	/**
	 * 买让球，最小的让球数
	 */
	private Float RADIO_RE = 0f;
	/**
	 * 让分比较
	 */
	private String RADIO_RE_COMPARE = "";
	/**
	 * 每次买入金额
	 */
	private String MONEYEACHMATCH = "50";

	/**
	 * 买入方
	 */
	private String BUYSIDE;
	/**
	 * 设置值只允许在第几节之前可以买入，可以为空，则忽略
	 */
	private Integer BEFORE_SE_NOW;

	@Override
	public void initParam(Map<String, String> paramMap) { 
		RADIO_RE = getFloatParamValue(paramMap, "RADIO_RE");
		RADIO_RE_COMPARE = getParamValue(paramMap, "RADIO_RE_COMPARE");
		MONEYEACHMATCH = getParamValue(paramMap, "MONEYEACHMATCH");
		BUYSIDE = getParamValue(paramMap, "BUYSIDE");
		BEFORE_SE_NOW = getIntegerParamValue(paramMap, "BEFORE_SE_NOW");
	}

	/**
	 * 描述
	 */
	public String desc(BetRule betRule) {
		Map<String, String> paramMap = GsonUtil.getGson().fromJson(betRule.getParam(),
				HashMap.class);
		this.initParam(paramMap);
		StringBuffer sb = new StringBuffer();
		sb.append("买入");
		sb.append(BUYSIDE.equals("H") ? betRule.getTeam_h() : betRule.getTeam_c());
		sb.append("让球");
		sb.append(RADIO_RE_COMPARE);
		sb.append(RADIO_RE);
		sb.append(",金额").append(MONEYEACHMATCH);
		return sb.toString();
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
			BetInfoDto betInfo = (BetInfoDto) ballEvent.getSource();
			// 是否有让球的投注
			if (betCondition(betInfo.getGid(), betInfo.getN_sw_R(), betInfo.getN_ratio_re_c(),
					betInfo.getSe_now(), betInfo.getN_strong())) {
				return true;
			}
		}
		return false;
	}

	private boolean betCondition(String gId, String sw_re, Object radiore, String seNow,
			String strong) {
		if (!this.getgId().equals(gId)) {
			throw new BusiException("不是匹配的gId:" + gId + " 规则gId:" + this.getgId());
		}
		if (!sw_re.equals("Y")) {
			throw new BusiException("让球买入已经关闭");
		}
		/*
		 * if (!(betTimeEachMatch(this.getBetStrategyContext().getAccount(),
		 * gId) < MAXEACHMATCH)) { throw new BusiException("超过本场最大买入次数:" +
		 * MAXEACHMATCH); }
		 */
		if (!fixRadioRe(radiore, strong)) {
			throw new BusiException("不是理想让球分，目前:" + radiore + ",规则:" + RADIO_RE_COMPARE + RADIO_RE);
		}
		if (!fixSeTime(seNow)) {
			throw new BusiException("不是小于第" + BEFORE_SE_NOW + "节时间，目前:" + seNow);
		}

		return true;
	}

	/**
	 * 合符要求的让球
	 * 
	 * @param radio
	 * @return
	 */
	private Boolean fixRadioRe(Object currentRadioRe, String strong) {
		if (RADIO_RE == null)
			return false;
		if (currentRadioRe == null)
			return false;
		Float radioRe = Float.valueOf(currentRadioRe.toString());
		radioRe = strong.equals("H") ? Math.abs(radioRe) : -Math.abs(radioRe);
		return ScriptUtil
				.eval(Float.valueOf(currentRadioRe.toString()) + RADIO_RE_COMPARE + RADIO_RE);
	}

	/**
	 * 在第几节之前才允许买入
	 * 
	 * @param radio
	 * @return
	 */
	private Boolean fixSeTime(String seNow) {
		if (BEFORE_SE_NOW == null)
			return true;
		if (seNow == null)
			return false;
		try {
			String quart = seNow.substring(1);
			return Integer.valueOf(quart) < BEFORE_SE_NOW;
		} catch (NumberFormatException e) {
			return false;
		}
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

	@Override
	public Object bet(BallEvent ballEvent) {
		String gtype = "BK";// 篮球
		String wtype = "RE";// 让球
		String betMoney = MONEYEACHMATCH; // 下注金额

		BetResp ftBetResp = null;
		log.info("===准备下注:" + ballEvent.getSource().toString());
		if (ballEvent.getSource() != null && ballEvent.getSource() instanceof BetInfoDto) {
			BetInfoDto betInfo = (BetInfoDto) ballEvent.getSource();
			try {
				if (false) {
					BkPreOrderViewResp bkPreOrderViewResp = RequestTool.getbkPreOrderView(uid,
							betInfo.getGid(), gtype, wtype, BUYSIDE);
					log.info("投注前信息：" + bkPreOrderViewResp);
					// 下注前需要再次检查一下次条件
					if (betCondition(betInfo.getGid(), betInfo.getN_sw_R(),
							bkPreOrderViewResp.getSpread(), betInfo.getSe_now(),
							bkPreOrderViewResp.getStrong())) {
						log.info(getStrategyName() + "准备下注:" + ballEvent.getSource().toString());
						if (getBetStrategyContext().isAllowBet()) {
							ftBetResp = RequestTool.bkbet(uid, betInfo.getGid(), gtype, betMoney,
									wtype, BUYSIDE, bkPreOrderViewResp);
						}
					}
				} else {

					ftBetResp = BetResp.debugBetResp();
					ftBetResp.setTicket_id("11212121");
					ftBetResp.setGid(betInfo.getGid());
					ftBetResp.setGold(betMoney);
					ftBetResp.setGtype(gtype);
					ftBetResp.setWtype(wtype);
					ftBetResp.setSpread("2");
					ftBetResp.setType(BUYSIDE);
					ftBetResp.setCode("560");
				}

			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		log.info("===下注结果:" + ftBetResp.toString());
		return ftBetResp;
	}

}