package com.ewing.order.busi.ball.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.dao.BetLogDao;
import com.ewing.order.busi.ball.ddl.BetLog;
import com.ewing.order.util.DataFormat;

/**
 *
 * @author tansonlam
 * @create 2018年7月24日
 */
@Component
public class BetLogService {
	@Resource
	private BetLogDao betLogDao;

	@Transactional(rollbackOn = { Exception.class })
	public void save(String account, BetLog betLog) {
		betLog.setAccount(account);
		betLogDao.save(betLog);
	}

	public List<BetLog> findSucBet(String account, String gId) {
		return betLogDao.findSucBet(account, gId);
	}

	public List<BetLog> findEachDay(String account, String gtype) {
		String startTime = DataFormat.DateToString(new Date(), DataFormat.DATE_ZEROTIME_FORMAT);
		String endTime = DataFormat.DateToString(new Date(), DataFormat.DATE_FULLTIME_FORMAT);
		return betLogDao.findEachDay(account, gtype, startTime, endTime);
	}
}
