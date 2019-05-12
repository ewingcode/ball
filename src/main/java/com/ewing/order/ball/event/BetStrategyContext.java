package com.ewing.order.ball.event;

import java.util.List;

import com.ewing.order.ball.BallLoginCache;
import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.busi.ball.service.BetLogService;
import com.ewing.order.busi.ball.service.BetRuleService;
import com.ewing.order.busi.ball.service.BwContinueService;
import com.ewing.order.common.prop.BallmatchProp;

public class BetStrategyContext {

	private BetLogService betLogService;

	private BetRuleService betRuleService;
	
	private BwContinueService bwContinueService;

	private String account;
	
	private String ballAccount;

	private String uid;

	private String ptype;

	private String gtype;

	private boolean allowBet = BallmatchProp.allowautobet;

	private WrapDataCallBack<List<BetInfoDto>> wrapDataCallBack;

	public List<BetInfoDto> getBetInfoList() {
		return wrapDataCallBack.getData();
	}

	public BetStrategyContext setBetInfoList(WrapDataCallBack<List<BetInfoDto>> wrapDataCallBack) {
		this.wrapDataCallBack = wrapDataCallBack;
		return this;
	}

	public String getGtype() {
		return gtype;
	}

	public BetStrategyContext setGtype(String gtype) {
		this.gtype = gtype;
		return this;
	}

	public String getPtype() {
		return ptype;
	}

	public BetStrategyContext setPtype(String ptype) {
		this.ptype = ptype;
		return this;
	}

	public boolean isAllowBet() {
		return allowBet;
	}

	public void setAllowBet(boolean allowBet) {
		this.allowBet = allowBet;
	}

	public String getUid() {
		return BallLoginCache.getLoginResp(account).getUid();
	}

	public BetStrategyContext setUid(String uid) {
		this.uid = uid;
		return this;
	}
	
	public String getBallAccount() {
		return ballAccount;
	}

	public BetStrategyContext setBallAccount(String ballAccount) {
		this.ballAccount = ballAccount;
		return this;
	}

	public String getAccount() {
		return account;
	}

	public BetStrategyContext setAccount(String account) {
		this.account = account;
		return this;
	}

	public BetLogService getBetLogService() {
		return betLogService;
	}

	public BetRuleService getBetRuleService() {
		return betRuleService;
	}
	
	public BwContinueService getBwContinueService() {
		return bwContinueService;
	}

	public BetStrategyContext setBetLogService(BetLogService betLogService) {
		this.betLogService = betLogService;
		return this;
	}
	
	public BetStrategyContext setBwContinueService(BwContinueService bwContinueService) {
		this.bwContinueService = bwContinueService;
		return this;
	}
	
	

	public BetStrategyContext setBetRuleService(BetRuleService betRuleService) {
		this.betRuleService = betRuleService;
		return this;
	}

}
