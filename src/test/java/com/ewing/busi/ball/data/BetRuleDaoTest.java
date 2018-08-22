package com.ewing.busi.ball.data;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ewing.order.Door;
import com.ewing.order.busi.ball.service.BetRuleService;

/**
 *
 * @author tansonlam
 * @create 2018年7月24日
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Door.class)
public class BetRuleDaoTest {
	@Resource
	private BetRuleService betRuleService;

	@Test 
	public void testupdate2Ineff() {

		try {
			betRuleService.update2Success(3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
