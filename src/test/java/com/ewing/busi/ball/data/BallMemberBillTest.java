package com.ewing.busi.ball.data;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ewing.order.Door;
import com.ewing.order.ball.BallMember;
import com.ewing.order.ball.BetCollector;
import com.ewing.order.ball.login.LoginResp; 

/**
 *
 * @author tansonlam
 * @create 2018年7月25日
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Door.class)
public class BallMemberBillTest {
	@Resource
	private BallMember ballMember;
	@Resource
	private BetCollector collectBetInfo;

	@Test
	public void testCollect() {

		try {

			LoginResp loginResp = ballMember.login("tansonLAM83", "523123ZX");
			ballMember.addBkListener("tansonLAM83", loginResp.getUid());
			collectBetInfo.login("tansonLAM48", "523123ZX");
			collectBetInfo.startCollectBasketInfo();
			TimeUnit.HOURS.sleep(24);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
