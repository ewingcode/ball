package com.ewing.order.busi.ball.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ewing.order.ball.shared.GameStatus;
import com.ewing.order.busi.ball.dao.BetInfoDao;
import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.util.BeanCopy;

/**
 *
 * @author tansonlam
 * @create 2018年7月24日
 */
@Component
public class BetInfoService {
	@Resource
	private BetInfoDao betInfoDao;

	public BetInfo findByGameId(String gameId) {
		return betInfoDao.findByGameId(gameId);
	}

	@Transactional(rollbackOn = { Exception.class })
	public void updateReadyBetInfo(List<BetInfo> list) {
		for (BetInfo betInfo : list) {

			betInfo.setStatus(GameStatus.READY);
			if (StringUtils.isEmpty(betInfo.getGid()))
				continue;
			BetInfo yetBetInfo = betInfoDao.findByGameId(betInfo.getGid());
			if (yetBetInfo == null) {
				betInfoDao.save(betInfo);
			} else {
				BeanCopy.copy(yetBetInfo, betInfo, true);
				betInfoDao.update(yetBetInfo);
			}
		}
	}

	@Transactional(rollbackOn = { Exception.class })
	public void updateRunningBetInfo(List<BetInfo> list) {
		for (BetInfo betInfo : list) {
			betInfo.setStatus(GameStatus.READY);
			if (StringUtils.isEmpty(betInfo.getGid()))
				continue;
			BetInfo yetBetInfo = betInfoDao.findByGameId(betInfo.getGid());
			if (yetBetInfo != null && !yetBetInfo.equals(GameStatus.RUNNING)) {
				yetBetInfo.setStatus(GameStatus.RUNNING);
				betInfoDao.update(yetBetInfo);
			}
		}
	}

}
