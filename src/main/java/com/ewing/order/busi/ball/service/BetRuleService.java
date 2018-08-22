package com.ewing.order.busi.ball.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.dao.BetRuleDao;
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

	public List<BetRule> findRule(String account, String gtype, String ptype) {
		return betRuleDao.findRule(account, gtype, ptype);
	}

	@Transactional
	public int update2Success(Integer ruleId) {
		return betRuleDao.update2Success(ruleId);
	}

	@Transactional
	public void addRule(BetRule betRule) {
		baseDao.save(betRule);
	}
}
