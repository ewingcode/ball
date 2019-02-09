package com.ewing.order.busi.ball.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.aliyuncs.utils.StringUtils;
import com.ewing.order.ball.BallAutoBet;
import com.ewing.order.ball.shared.BetRuleStatus;
import com.ewing.order.ball.shared.GtypeStatus;
import com.ewing.order.ball.shared.PtypeStatus;
import com.ewing.order.busi.ball.dao.BetAutoBuyDao;
import com.ewing.order.busi.ball.ddl.BetAutoBuy;
import com.ewing.order.busi.ball.ddl.BetRule;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.core.jpa.BaseDao;

/**
 *
 * @author tansonlam
 * @create 2018年8月2日
 */
@Component
public class BetAutoBuyService {
	@Resource
	private BetAutoBuyDao betAutoBuyDao;
	@Resource
	private BaseDao baseDao;
	@Resource
	private BallAutoBet ballAutoBet;
	@Resource
	private BetRuleService betRuleService;

	public List<BetAutoBuy> findAll() {
		return betAutoBuyDao.findAll();
	}

	public BetAutoBuy find(String account) {
		return betAutoBuyDao.find(account);
	}

	@Transactional
	public void updateIsEff(String account, String isEff, String phone, String money,Integer isTest,Integer continueMaxMatch)
			throws IllegalAccessException, InvocationTargetException {
		BetAutoBuy betAutoBuy = betAutoBuyDao.find(account);
		if (betAutoBuy != null) {
			betAutoBuy.setIseff(isEff);
			betAutoBuy.setIs_login(IsEff.INEFFECTIVE);
			betAutoBuy.setPhone(phone);
			baseDao.update(betAutoBuy);
		} else {
			if (!StringUtils.isEmpty(isEff) && IsEff.EFFECTIVE.equals(isEff)) {
				betAutoBuy = new BetAutoBuy();
				betAutoBuy.setAccount(account);
				betAutoBuy.setPwd(ballAutoBet.getPwd4Cache(account));
				betAutoBuy.setIseff(IsEff.EFFECTIVE);
				betAutoBuy.setIs_login(IsEff.INEFFECTIVE);
				betAutoBuy.setIsallow(IsEff.INEFFECTIVE);
				betAutoBuy.setPhone(phone);
				baseDao.save(betAutoBuy);
			}
		}
		betRuleService.updateRule(account, GtypeStatus.BK, PtypeStatus.ROLL, money,isTest,continueMaxMatch);
	}
	
	@Transactional
	public void activeAccount(String account,String isAllow)
			throws IllegalAccessException, InvocationTargetException {
		BetAutoBuy betAutoBuy = betAutoBuyDao.find(account);
		if (betAutoBuy != null) {
			betAutoBuy.setIsallow(isAllow);
			baseDao.update(betAutoBuy);
		} 
	}

	@Transactional
	public void updateLoginOut(String account) {
		BetAutoBuy betAutoBuy = betAutoBuyDao.find(account);
		if (betAutoBuy != null) {
			betAutoBuy.setIs_login(IsEff.INEFFECTIVE);
			baseDao.update(betAutoBuy);
		}
	}

	@Transactional
	public void updateLoginIn(String account) {
		BetAutoBuy betAutoBuy = betAutoBuyDao.find(account);
		if (betAutoBuy != null) {
			betAutoBuy.setIs_login(IsEff.EFFECTIVE);
			baseDao.update(betAutoBuy);
		}
	}
}
