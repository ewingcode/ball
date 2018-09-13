package com.ewing.busi.ball.betway;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ewing.order.Door;
import com.ewing.order.ball.BetCollector;
import com.ewing.order.ball.BetCollector.CollectDataPool;
import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.ball.util.CalUtil;
import com.ewing.order.busi.ball.ddl.BetInfo;
import com.ewing.order.busi.ball.ddl.BetRollInfo;
import com.ewing.order.core.jpa.BaseDao;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Door.class)
public class DataPush {
	private static Logger log = LoggerFactory.getLogger(CalData.class);
	@Resource
	private BaseDao baseDao;
	DecimalFormat fnum = new DecimalFormat("##0.00");
	DecimalFormat fnum2 = new DecimalFormat("##0.0000");
	DecimalFormat fnum3 = new DecimalFormat("##0.000");
	@Test
	public void pushData() {
		// 结果：【+】赢83.0000,买入【大】,按方向:反向操作,买入分数:155.5,总分结果:156,Q4-全场得分:-0.0380,持续时间64,初率:0.0717,买入率:0.0247,全场率:0.0627,大回报:0.830,小回报:0.830
		// 篮球俱乐部友谊赛 亚洛瓦 vs 卡雷西 2018-09-10
		// 10:00:00,比赛ID：2602133,开始计算滚球ID:117033,滚球ID：117042,场节:Q4,时间:438,初盘：
		// 173.5,最大滚球分:175.5,最小滚球分:147.5
		String gId = "2602133";
		List<BetRollInfo> rollList = baseDao.find(
				"select * from bet_roll_info where gid in (" + gId + ")   and se_now='Q4' order by gId,id ",
				BetRollInfo.class);

		BetInfo betInfo = baseDao.findOne("select * from bet_info where gid = '" + gId + "' ",
				BetInfo.class);
		BetInfoDto betInfoDto = new BetInfoDto();
		BeanUtils.copyProperties(betInfo, betInfoDto);
		BetCollector.CollectDataPool.bkRollList.add(betInfoDto);
		for (BetRollInfo betRollInfo : rollList) {

			if (betRollInfo.getId() != null) {
			
				StringBuffer sb = new StringBuffer();
				sb.append("ID：").append(betRollInfo.getId());
				sb.append(",分数:" + betRollInfo.getRatio_rou_c()).append(",总分：")
						.append(StringUtils.leftPad(betRollInfo.getSc_total(), 3, "  ")).append(",")
						.append(betRollInfo.getSe_now())
						.append(" 节剩余（秒）:" + StringUtils.leftPad(betRollInfo.getT_count(), 3, " "))
						.append(",Q4-全场得分:")
						.append(fnum2.format(CalUtil.computeScoreSec4Quartz(betRollInfo)
								- CalUtil.computeScoreSec4Alltime(betRollInfo)))
						.append(" 每节进球（秒）："
								+ fnum2.format(CalUtil.computeScoreSec4Quartz(betRollInfo)))
						.append(" 全场进球（秒）："
								+ fnum2.format(CalUtil.computeScoreSec4Alltime(betRollInfo))) 
						.append(" [Q1]：" + betRollInfo.getSc_Q1_total())
						.append(" [Q2]：" + betRollInfo.getSc_Q2_total())
						.append(" [Q3]：" + betRollInfo.getSc_Q3_total())
						.append(" [Q4]：" + betRollInfo.getSc_Q4_total());
				log.info(sb.toString());
				BetCollector.CollectDataPool.bkRollList.get(0)
						.setLastUpdate(new Timestamp(System.currentTimeMillis()));
				CollectDataPool.putRollDetail(betRollInfo);
			}
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			TimeUnit.SECONDS.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
