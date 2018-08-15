package com.ewing.order.busi.ball.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.ddl.BetRule;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.core.jpa.BaseDao;

/**
 *
 * @author tansonlam
 * @create 2018年7月25日
 */
@Component
public class BetRuleDao {
	@Resource
	private BaseDao baseDao;

	public List<BetRule> findRule(String account) {
		return baseDao.find("account='" + account + "' and iseff='" + IsEff.EFFECTIVE + "' order by level desc",
				BetRule.class);
	}

}
