package com.ewing.order.busi.ball.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.ddl.BetLog;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.core.jpa.BaseDao;

/**
 *
 * @author tansonlam
 * @create 2018年7月25日
 */
@Component
public class BetLogDao {
	@Resource
	private BaseDao baseDao;

	public void save(BetLog betLog) {
		baseDao.save(betLog);
	}
	
	public BetLog findById(Integer id){
		return baseDao.findOne(id, BetLog.class);
	}

	public List<BetLog> findSucBet(String account, String gId) {
		return baseDao.find("account='" + account + "' and gid='" + gId + "' and code = '560'",
				BetLog.class);
	}
	
	public List<BetLog> findNotNofity(String account) {
		return baseDao.find(" code = '560' and account='" + account + "' and bet_rule_id is not null and  is_notify='" + IsEff.INEFFECTIVE + "'",
				BetLog.class);
	}

	public List<BetLog> findEachDay(String account, String gtype, String startTime,
			String endTime) {
		return baseDao.find(
				"account='" + account + "' and gtype='" + gtype + "' and create_time>='" + startTime
						+ "' and create_time <='" + endTime + "' and errormsg is null",
				BetLog.class);
	}

}
