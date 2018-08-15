package com.ewing.order.busi.ball.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.ball.dao.BetBillDao;
import com.ewing.order.busi.ball.ddl.BetBill;
import com.ewing.order.util.BeanCopy;

/**
 *
 * @author tansonlam
 * @create 2018年7月24日
 */
@Component
public class BetBillService {
	@Resource
	private BetBillDao betBillDao;

	@Transactional(rollbackOn = { Exception.class })
	public void saveBill(String account,String date, List<BetBill> list) {
		for (BetBill betBill : list) {
			betBill.setAccount(account);
			betBill.setDate(date);
			BetBill yetBetBill = betBillDao.find(betBill.getAccount(), betBill.getW_id());
			if (yetBetBill == null) {
				betBillDao.save(betBill);
			} else {
				BeanCopy.copy(yetBetBill, betBill, true);
				betBillDao.update(yetBetBill);
			}
		}
	}

}
