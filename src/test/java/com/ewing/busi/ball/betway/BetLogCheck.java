package com.ewing.busi.ball.betway;

import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ewing.order.Door;
import com.ewing.order.busi.ball.dao.BetInfoDao;
import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.busi.ball.ddl.BetLog;
import com.ewing.order.core.jpa.BaseDao;

/**
 *
 * @author tansonlam
 * @create 2018年9月10日
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Door.class)
public class BetLogCheck {
	private static Logger log = LoggerFactory.getLogger(BetLogCheck.class);
	@Resource
	private BaseDao baseDao;
	@Resource
	private BetInfoDao betInfoDao;
	DecimalFormat fnum2 = new DecimalFormat("##0.0000");
	@Test
	public void checkResult() {
		String startTime = "2018-09-10";
		String endTime = "2018-09-11";
		List<BetLog> entityList = baseDao.find(
				"select * from bet_log where create_time>='"+startTime+"' and create_time<='"+endTime+"'  ",
				BetLog.class);
		Float winTotal = 0f;
		int bet_suc=0;
		int bet_fail=0;
		for (BetLog betLog : entityList) {
			BetInfo betInfo = betInfoDao.findByGameId(betLog.getGid());

			Float scoreDistance = Float.valueOf(betInfo.getSc_total())
					- Float.valueOf(betLog.getSpread());
			boolean isWin = false;
			float winMoney = 0f;
			if (betLog.getType().equals("H") && scoreDistance < 0) {
				isWin = true;
				winMoney = Float.valueOf(betLog.getGold()) * Float.valueOf(betLog.getIoratio());
			} else if (betLog.getType().equals("C") && scoreDistance > 0) {
				isWin = true;
				winMoney = Float.valueOf(betLog.getGold()) * Float.valueOf(betLog.getIoratio());
			} else {
				winMoney = -Float.valueOf(betLog.getGold());
			}
			winTotal += winMoney;
			StringBuffer sb = new StringBuffer();
			if (isWin) {
				sb.append(",结果：赢");
				bet_suc++;
			} else {
				sb.append(",结果：输");
				bet_fail++;
			}
			sb.append(" " + betInfo.getLeague());
			sb.append(" " + betInfo.getTeam_h()).append(" vs ").append(betInfo.getTeam_c())
					.append(" " + betInfo.getDatetime());
			sb.append(",总分:").append(betInfo.getSc_total());
			log.info(sb.toString());
		}
		StringBuffer total = new StringBuffer();
		total.append("时间").append(startTime).append("~").append(endTime)
		.append(",比赛总数:").append((bet_suc+bet_fail)).append(",成功率:").append(fnum2.format(bet_suc/((bet_suc+bet_fail)*1f)));
	}
}
