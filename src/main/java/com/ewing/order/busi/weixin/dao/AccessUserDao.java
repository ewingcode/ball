/**
 * 
 */
package com.ewing.order.busi.weixin.dao;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.ewing.order.busi.weixin.ddl.AccessUser;
import com.ewing.order.core.jpa.BaseDao;

/**
 * @author 47652
 *
 * @since 2017年2月9日
 */
@Component("accessUserDao")
public class AccessUserDao {

	@Resource(name = "baseDao")
	private BaseDao baseDao;

	@Transactional
	public void insert(AccessUser accessUser) {
		baseDao.save(accessUser);
	}

	public AccessUser getOne(String openId) {

		return baseDao.findOne("open_id='" + openId + "'", AccessUser.class);
	}

}
