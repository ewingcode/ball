/**
 * 
 */
package com.ewing.order.busi.weixin.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.weixin.ddl.PayHistory;
import com.ewing.order.core.jpa.BaseDao;

/**
 * @author 47652
 *
 * @since 2017年2月15日
 */
@Component
public class PayHistoryDao {

	@Resource
	private BaseDao baseDao;

	public void insert(PayHistory payHistory) {

		baseDao.save(payHistory);
	}
	
	
	public void updatePayStatus(int id, int status){
		PayHistory payHistory = new PayHistory();
		payHistory.setId(id);
		payHistory.setStatus(status);
		baseDao.update(payHistory);
	}

}
