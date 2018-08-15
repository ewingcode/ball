package com.ewing.order.busi.ball.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.dao.BetRuleDao;
import com.ewing.order.busi.ball.ddl.BetRule;

/**
 *
 * @author tansonlam
 * @create 2018年8月2日
 */
@Component
public class BetRuleService {
	@Resource
	private BetRuleDao betRuleDao;

	public List<BetRule> findRule(String account) {
		return betRuleDao.findRule(account);
	}
}
