package com.ewing.order.busi.ball.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ewing.order.ball.shared.GameStatus;
import com.ewing.order.busi.ball.dao.BetInfoDao;
import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.busi.ball.ddl.BetRollInfo;
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
			// 计算滚球变化数据
			if (betInfo.getGtype().equals("BK")) {
				calBasketball(betInfo);
			}
			
			if (yetBetInfo == null) {
				betInfoDao.save(betInfo);
			} else {
				BeanCopy.copy(yetBetInfo, betInfo, true);
				betInfoDao.update(yetBetInfo);
			}
		}
	}
	
	private void calBasketball(BetInfo betInfo) {
		if (!StringUtils.isEmpty(betInfo.getRatio())) {
			Float ratio_re_c = Float.valueOf(
					(betInfo.getStrong().equals("H") ? "" : "-") + betInfo.getRatio());
			betInfo.setRatio_re_c(ratio_re_c);
		}
		if (!StringUtils.isEmpty(betInfo.getRatio_o())) {
			Float ratio_rou_c = Float.valueOf(betInfo.getRatio_o().replace("大", "").trim());
			betInfo.setRatio_rou_c(ratio_rou_c);
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
