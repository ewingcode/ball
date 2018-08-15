package com.ewing.busi.ball.data;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ewing.order.Door;
import com.ewing.order.busi.ball.dao.BetInfoDao;
import com.ewing.order.busi.ball.dao.BetRollInfoDao;
import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.busi.ball.ddl.RollGameCompute;

/**
 *
 * @author tansonlam
 * @create 2018年7月24日
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Door.class)
public class BetInfoDaoTest {
	@Resource
	private BetInfoDao betInfoDao;
	@Resource
	private BetRollInfoDao betRollInfoDao;

	@Test
	public void testQuery() {

		try {
			RollGameCompute b = betRollInfoDao.computeMinAndMax("2582421");
			System.out.println(b);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
