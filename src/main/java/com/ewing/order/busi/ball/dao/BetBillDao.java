package com.ewing.order.busi.ball.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.ddl.BetBill;
import com.ewing.order.core.jpa.BaseDao;

/**
 *
 * @author tansonlam
 * @create 2018年7月25日
 */
@Component
public class BetBillDao {
	@Resource
	private BaseDao baseDao;

	public void save(BetBill betBill) {
		baseDao.save(betBill);
	}

	public void update(BetBill betBill) {
		baseDao.update(betBill);
	}

	public BetBill find(String account, String wId) {
		return baseDao.findOne("account='" + account + "' and w_id='" + wId+"'", BetBill.class);
	}

}
