package com.ewing.order.busi.ball.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.dao.BetAutoBuyDao;
import com.ewing.order.busi.ball.ddl.BetAutoBuy;
import com.ewing.order.common.contant.IsEff;
import com.ewing.order.core.jpa.BaseDao;

/**
 *
 * @author tansonlam
 * @create 2018年8月2日
 */
@Component
public class BetAutoBuyService {
	@Resource
	private BetAutoBuyDao betAutoBuyDao;
	@Resource
	private BaseDao baseDao;

	public List<BetAutoBuy> findAll() {
		return betAutoBuyDao.findAll();
	}

	@Transactional
	public void updateLoginOut(String account) {
		BetAutoBuy betAutoBuy = betAutoBuyDao.find(account);
		if (betAutoBuy != null) {
			betAutoBuy.setIs_login(IsEff.INEFFECTIVE);
			baseDao.update(betAutoBuy);
		}
	}

	@Transactional
	public void updateLoginIn(String account) {
		BetAutoBuy betAutoBuy = betAutoBuyDao.find(account);
		if (betAutoBuy != null) {
			betAutoBuy.setIs_login(IsEff.EFFECTIVE);
			baseDao.update(betAutoBuy);
		}
	}
}
