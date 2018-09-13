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
		/*
		 *  结果：【+】赢83.0000,买入【大】,按方向:反向操作,买入分数:155.5,总分结果:156,Q4-全场得分:-0.0380,持续次数10,持续时间64,初率:0.0717,买入率:0.0247,全场率:0.0627,大回报:0.830,小回报:0.830 篮球俱乐部友谊赛 亚洛瓦 vs 卡雷西 2018-09-10 10:00:00,比赛ID：2602133,开始计算滚球ID:117033,滚球ID：117042,场节:Q4,时间:438,初盘： 173.5,最大滚球分:175.5,最小滚球分:147.5
结果：【+】赢80.0000,买入【大】,按方向:反向操作,买入分数:153.5,总分结果:162,Q4-全场得分:-0.0334,持续次数11,持续时间53,初率:0.0617,买入率:0.0303,全场率:0.0637,大回报:0.800,小回报:0.800 日本B篮球联赛初期杯 福岛火绊 vs 岩手大牛 2018-09-07 03:00:00,比赛ID：2599529,开始计算滚球ID:90921,滚球ID：90934,场节:Q4,时间:468,初盘： 149.5,最大滚球分:158.5,最小滚球分:129.5
结果：【+】赢76.0000,买入【大】,按方向:反向操作,买入分数:166.5,总分结果:170,Q4-全场得分:-0.0321,持续次数10,持续时间50,初率:0.0667,买入率:0.0380,全场率:0.0701,大回报:0.760,小回报:0.900 篮球俱乐部友谊赛 布林迪西 vs 莫尔纳 2018-09-06 14:30:00,比赛ID：2599361,开始计算滚球ID:90412,滚球ID：90422,场节:Q4,时间:416,初盘： 159.5,最大滚球分:188.0,最小滚球分:158.5
结果：【-】输100.0000,买入【大】,按方向:反向操作,买入分数:165.5,总分结果:153,Q4-全场得分:-0.0406,持续次数12,持续时间50,初率:0.0683,买入率:0.0278,全场率:0.0684,大回报:0.960,小回报:0.700 篮球俱乐部友谊赛 尼维斯 vs 利马帕 2018-09-05 12:00:00,比赛ID：2599109,开始计算滚球ID:86557,滚球ID：86600,场节:Q4,时间:456,初盘： 162.5,最大滚球分:181.5,最小滚球分:162.5
结果：【+】赢90.0000,买入【大】,按方向:反向操作,买入分数:165.5,总分结果:167,Q4-全场得分:-0.0290,持续次数10,持续时间60,初率:0.0667,买入率:0.0402,全场率:0.0692,大回报:0.900,小回报:0.760 WNBA季后赛 亚特兰大梦想 vs 华盛顿奇异 2018-09-04 20:00:00,比赛ID：2597982,开始计算滚球ID:84949,滚球ID：84962,场节:Q4,时间:376,初盘： 159.5,最大滚球分:184.5,最小滚球分:155.5
结果：【+】赢90.0000,买入【大】,按方向:反向操作,买入分数:165.5,总分结果:172,Q4-全场得分:-0.0393,持续次数11,持续时间67,初率:0.0700,买入率:0.0298,全场率:0.0691,大回报:0.900,小回报:0.760 篮球俱乐部友谊赛 沙隆兰斯 vs 维勒布鲁克袋鼠篮球队 2018-09-04 13:00:00,比赛ID：2598836,开始计算滚球ID:83949,滚球ID：83974,场节:Q4,时间:432,初盘： 166.5,最大滚球分:180.5,最小滚球分:163.5
结果：【+】赢83.0000,买入【大】,按方向:反向操作,再反转大于阀值0.0450,买入分数:176.5,总分结果:181,Q4-全场得分:0.0582,持续次数11,持续时间148,初率:0.0717,买入率:0.1111,全场率:0.0529,大回报:0.830,小回报:0.830 篮球俱乐部友谊赛 俄斯特拉发 vs 奥洛穆茨科 2018-09-04 11:30:00,比赛ID：2598794,开始计算滚球ID:83532,滚球ID：83557,场节:Q4,时间:357,初盘： 172,最大滚球分:185.5,最小滚球分:161.5

		 */
		String gId = "2599529";
		List<BetRollInfo> rollList = baseDao.find("select * from bet_roll_info where gid in (" + gId
				+ ")   and se_now='Q4' order by gId,id ", BetRollInfo.class);

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
