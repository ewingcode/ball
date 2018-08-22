package com.ewing.order.ball.event;

import com.ewing.order.busi.ball.service.BetLogService;
import com.ewing.order.busi.ball.service.BetRuleService;

public class BetStrategyContext {

	private BetLogService betLogService;

	private BetRuleService betRuleService;

	private String account;

	private String uid;

	private String ptype;

	private String gtype;

	private boolean allowBet = false;

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
		return uid;
	}

	public BetStrategyContext setUid(String uid) {
		this.uid = uid;
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

	public BetStrategyContext setBetLogService(BetLogService betLogService) {
		this.betLogService = betLogService;
		return this;
	}

	public BetStrategyContext setBetRuleService(BetRuleService betRuleService) {
		this.betRuleService = betRuleService;
		return this;
	}

}
