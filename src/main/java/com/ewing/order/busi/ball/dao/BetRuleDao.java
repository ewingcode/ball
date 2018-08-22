package com.ewing.order.busi.ball.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.hibernate.engine.spi.QueryParameters;
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

	public List<BetRule> findRule(String account, String gtype, String ptype) {
		return baseDao.find(
				"account='" + account + "' and gtype='" + gtype + "' and ptype='" + ptype
						+ "' and iseff='" + IsEff.EFFECTIVE + "' order by level desc",
				BetRule.class);
	} 
	
	public int update2Success(Integer ruleId) {
		return baseDao.executeUpdate(
				"update bet_rule set iseff='" + IsEff.INEFFECTIVE + "'  where id=" + ruleId, new QueryParameters());
	}

}
