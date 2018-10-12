package com.ewing.order.busi.ball.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.ball.shared.BetRuleStatus;
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

	
	public BetRule findMainRule(String gtype,String ptype) {
		return baseDao.findOne("is_main='1' and gtype='" + gtype + "' and ptype='"
				+ ptype + "' and iseff='" + IsEff.EFFECTIVE + "'", BetRule.class);
	}
	public List<BetRule> findRule(String account, String status, String gtype, String ptype) {
		return baseDao.find("account='" + account + "' and gtype='" + gtype + "' and ptype='"
				+ ptype + "' and iseff='" + IsEff.EFFECTIVE + "'" + "  and status='" + status
				+ "' order by level desc", BetRule.class);
	}

	public int update2Success(Integer ruleId, Integer betLogId) {
		return baseDao.executeUpdate("update bet_rule set status='" + BetRuleStatus.SUCCESS
				+ "',bet_log_id=" + betLogId + "  where id=" + ruleId);
	}

	public int updateDesc(Integer ruleId, String msg) {
		return baseDao
				.executeUpdate("update bet_rule set `comment`='" + msg + "'  where id=" + ruleId);
	}

	public int update2Ineff(Integer ruleId) {
		return baseDao.executeUpdate("update bet_rule set iseff='" + IsEff.INEFFECTIVE
				+ "',`comment`='过期' where id=" + ruleId);
	}

}
