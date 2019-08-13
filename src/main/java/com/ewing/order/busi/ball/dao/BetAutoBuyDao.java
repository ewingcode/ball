package com.ewing.order.busi.ball.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.ddl.BetAutoBuy;
import com.ewing.order.core.jpa.BaseDao;

/**
 *
 * @author tansonlam
 * @create 2018年7月25日
 */
@Component
public class BetAutoBuyDao {
	@Resource
	private BaseDao baseDao;

	public List<BetAutoBuy> findAll() {
		return baseDao.find("1=1 order by iseff desc,account",BetAutoBuy.class);
	}

	public BetAutoBuy find(String account) {
		return baseDao.findOne("  account='" + account + "'", BetAutoBuy.class);
	}
}
