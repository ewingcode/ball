package com.ewing.order.busi.ball.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.ewing.order.ball.shared.BetRuleStatus;
import com.ewing.order.busi.ball.dao.BetRuleDao;
import com.ewing.order.busi.ball.dao.BwContinueDao;
import com.ewing.order.busi.ball.ddl.BetRule;
import com.ewing.order.core.jpa.BaseDao;

/**
 *
 * @author tansonlam
 * @create 2018年8月2日
 */
@Component
public class BetRuleService {
	@Resource
	private BetRuleDao betRuleDao;
	@Resource
	private BaseDao baseDao;
	@Resource
	private BwContinueDao bwContinueDao;

	@Transactional
	public void updateRule(String account, String gtype, String ptype, String money, Integer isTest,
			Integer continueMaxMatch, Integer continueStartLostnum)
			throws IllegalAccessException, InvocationTargetException {
		List<BetRule> ruleList = betRuleDao.findRule(account, BetRuleStatus.NOTSUCCESS, gtype,
				ptype);
		if (CollectionUtils.isEmpty(ruleList)) {
			copyNewRule(account, gtype, ptype, money);
		} else {
			BetRule betRule = ruleList.get(0);
			betRule.setMoney(money);
			betRule.setIsTest(isTest);
			betRule.setContinueMaxMatch(continueMaxMatch);
			betRule.setContinueStartLostnum(continueStartLostnum); 
			baseDao.update(betRule);
			if(continueMaxMatch==0)
				bwContinueDao.update2Cancel(betRule.getId());
		}
	}

	public void copyNewRule(String account, String gtype, String ptype, String money)
			throws IllegalAccessException, InvocationTargetException {
		BetRule mainBetRule = betRuleDao.findMainRule(gtype, ptype);
		BetRule newBetRule = new BetRule();
		BeanUtils.copyProperties(newBetRule, mainBetRule);
		newBetRule.setId(null);
		newBetRule.setIs_main("0");
		newBetRule.setAccount(account);
		newBetRule.setMoney(money);
		newBetRule.setCreateTime(null);
		newBetRule.setLastUpdate(null);
		baseDao.save(newBetRule);
	}

	public List<BetRule> findRule(String account, String status, String gtype, String ptype) {
		return betRuleDao.findRule(account, status, gtype, ptype);
	}

	@Transactional
	public int update2Success(Integer ruleId, Integer betLogId) {
		return betRuleDao.update2Success(ruleId, betLogId);
	}

	@Transactional
	public int updateDesc(Integer ruleId, String msg) {
		return betRuleDao.updateDesc(ruleId, msg);
	}

	@Transactional
	public int update2Ineff(Integer ruleId) {
		return betRuleDao.update2Ineff(ruleId);
	}

	@Transactional
	public void addRule(BetRule betRule) {
		baseDao.save(betRule);
	}
}
