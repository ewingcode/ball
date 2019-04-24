package com.ewing.order.busi.ball.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.ddl.BetRulePool;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.core.jpa.BaseDao;

/**
 *
 * @author tansonlam
 * @create 2018年7月25日
 */
@Component
public class BetRulePoolDao {
	@Resource
	private BaseDao baseDao;

	public List<BetRulePool> findAllRule() {
		return baseDao.find("iseff='" + IsEff.EFFECTIVE + "'", BetRulePool.class);
	}

	public BetRulePool findRuleByName(String name) {
		return baseDao.findOne("name = '" + name + "' and iseff='" + IsEff.EFFECTIVE + "'",
				BetRulePool.class);
	}

}
