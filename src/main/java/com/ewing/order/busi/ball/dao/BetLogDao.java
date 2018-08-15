package com.ewing.order.busi.ball.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.ddl.BetLog;
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

	public List<BetLog> findSucBet(String account, String gId) {
		return baseDao.find("account='" + account + "' and gid='" + gId + "' and errormsg is null",
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
