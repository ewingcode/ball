package com.ewing.order.busi.ball.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.dao.BetRulePoolDao;
import com.ewing.order.busi.ball.ddl.BetRulePool;
import com.google.common.collect.Lists;

/**
 *
 * @author tansonlam
 * @create 2019年4月23日
 */
@Component
public class BetRulePoolService {
	@Resource
	private BetRulePoolDao betRulePoolDao;

	public List<String> findAllRuleName() {
		List<String> list = Lists.newArrayList();
		List<BetRulePool> allRule = betRulePoolDao.findAllRule();
		if (CollectionUtils.isNotEmpty(allRule)) {
			for (BetRulePool betRulePool : allRule) {
				list.add(betRulePool.getName());
			}
		}
		return list;
	}
}
