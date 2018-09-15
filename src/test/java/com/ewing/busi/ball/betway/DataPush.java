package com.ewing.busi.ball.betway;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
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
import com.google.common.collect.Maps;

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
		 /**
		  * 结果：【+】赢77.0000,买入【大】,按方向:反向操作,买入分数:172.0,当时总分:143,预计总分:154.7459,预计总分2:173.4383,时间:421,总分结果:183,大回报:0.770,小回报:0.890,Q4-全场得分:-0.0444,持续次数10,持续时间119,初率:0.0617,买入率:0.0279,全场率:0.0723 FIBA男篮世界杯2019欧洲预选赛 意大利 vs 波兰 2018-09-14 14:15:00,比赛ID：2603295,开始计算滚球ID:126867,滚球ID：126928,场节:Q4,初盘： 149,最大滚球分:188.5,最小滚球分:150.5
2018-09-15 15:07:28 - [ INFO ] 结果：【-】输100.0000,买入【小】,按方向:反向操作,买入分数:139.5,当时总分:111,预计总分:154.5386,预计总分2:136.878,时间:454,总分结果:149,大回报:0.830,小回报:0.830,Q4-全场得分:0.0389,持续次数10,持续时间130,初率:0.0617,买入率:0.0959,全场率:0.0570 FIBA欧洲篮球赛预选赛2021 科索沃 vs 马其顿 2018-09-13 14:30:00,比赛ID：2602749,开始计算滚球ID:122929,滚球ID：122940,场节:Q4,初盘： 148.5,最大滚球分:153.0,最小滚球分:128.5
2018-09-15 15:07:28 - [ INFO ] 结果：【-】输100.0000,买入【大】,按方向:反向操作,买入分数:176.5,当时总分:149,预计总分:166.5871,预计总分2:178.5368,时间:397,总分结果:172,大回报:0.830,小回报:0.830,Q4-全场得分:-0.0301,持续次数10,持续时间124,初率:0.0650,买入率:0.0443,全场率:0.0744 篮球俱乐部友谊赛 帕伦西亚 vs 毕尔包 2018-09-12 14:30:00,比赛ID：2603547,开始计算滚球ID:119662,滚球ID：119682,场节:Q4,初盘： 154.5,最大滚球分:184.5,最小滚球分:153.5
2018-09-15 15:07:28 - [ INFO ] 结果：【-】输100.0000,买入【小】,按方向:反向操作,买入分数:153.5,当时总分:125,预计总分:166.62021,预计总分2:151.9658,时间:426,总分结果:164,大回报:0.830,小回报:0.830,Q4-全场得分:0.0344,持续次数10,持续时间78,初率:0.0683,买入率:0.0977,全场率:0.0633 篮球俱乐部友谊赛 威尼斯雷耶 vs 甘都篮球队 2018-09-12 14:30:00,比赛ID：2603568,开始计算滚球ID:119672,滚球ID：119694,场节:Q4,初盘： 164.5,最大滚球分:165.5,最小滚球分:141.5
2018-09-15 15:07:28 - [ INFO ] 结果：【+】赢76.0000,买入【大】,按方向:反向操作,买入分数:155.5,当时总分:134,预计总分:143.5192,预计总分2:155.0596,时间:326,总分结果:156,大回报:0.760,小回报:0.900,Q4-全场得分:-0.0354,持续次数10,持续时间123,初率:0.0550,买入率:0.0292,全场率:0.0646 篮球女子俱乐部友谊赛 喀山诺奇卡(女) vs 切瓦卡塔沃洛格达(女) 2018-09-12 07:00:00,比赛ID：2603400,开始计算滚球ID:118397,滚球ID：118408,场节:Q4,初盘： 131.5,最大滚球分:163.5,最小滚球分:131.5
2018-09-15 15:07:28 - [ INFO ] 结果：【+】赢83.0000,买入【大】,按方向:反向操作,买入分数:155.5,当时总分:123,预计总分:133.8186,预计总分2:150.4626,时间:438,总分结果:156,大回报:0.830,小回报:0.830,Q4-全场得分:-0.0380,持续次数10,持续时间110,初率:0.0717,买入率:0.0247,全场率:0.0627 篮球俱乐部友谊赛 亚洛瓦 vs 卡雷西 2018-09-10 10:00:00,比赛ID：2602133,开始计算滚球ID:117033,滚球ID：117042,场节:Q4,初盘： 173.5,最大滚球分:175.5,最小滚球分:147.5
2018-09-15 15:07:28 - [ INFO ] 结果：【+】赢80.0000,买入【大】,按方向:反向操作,买入分数:153.5,当时总分:123,预计总分:137.601,预计总分2:153.0498,时间:471,总分结果:162,大回报:0.800,小回报:0.800,Q4-全场得分:-0.0328,持续次数10,持续时间70,初率:0.0617,买入率:0.0310,全场率:0.0638 日本B篮球联赛初期杯 福岛火绊 vs 岩手大牛 2018-09-07 03:00:00,比赛ID：2599529,开始计算滚球ID:90921,滚球ID：90932,场节:Q4,初盘： 149.5,最大滚球分:158.5,最小滚球分:129.5
2018-09-15 15:07:28 - [ INFO ] 结果：【+】赢76.0000,买入【大】,按方向:反向操作,买入分数:166.5,当时总分:139,预计总分:154.808,预计总分2:168.1616,时间:416,总分结果:170,大回报:0.760,小回报:0.900,Q4-全场得分:-0.0321,持续次数10,持续时间77,初率:0.0667,买入率:0.0380,全场率:0.0701 篮球俱乐部友谊赛 布林迪西 vs 莫尔纳 2018-09-06 14:30:00,比赛ID：2599361,开始计算滚球ID:90412,滚球ID：90422,场节:Q4,初盘： 159.5,最大滚球分:188.0,最小滚球分:158.5
2018-09-15 15:07:28 - [ INFO ] 结果：【-】输100.0000,买入【大】,按方向:反向操作,买入分数:165.5,当时总分:133,预计总分:145.6768,预计总分2:164.1904,时间:456,总分结果:153,大回报:0.960,小回报:0.700,Q4-全场得分:-0.0406,持续次数12,持续时间77,初率:0.0683,买入率:0.0278,全场率:0.0684 篮球俱乐部友谊赛 尼维斯 vs 利马帕 2018-09-05 12:00:00,比赛ID：2599109,开始计算滚球ID:86557,滚球ID：86600,场节:Q4,初盘： 162.5,最大滚球分:181.5,最小滚球分:162.5
2018-09-15 15:07:28 - [ INFO ] 结果：【+】赢90.0000,买入【大】,按方向:反向操作,买入分数:165.5,当时总分:140,预计总分:155.1152,预计总分2:166.0192,时间:376,总分结果:167,大回报:0.900,小回报:0.760,Q4-全场得分:-0.0290,持续次数10,持续时间137,初率:0.0667,买入率:0.0402,全场率:0.0692 WNBA季后赛 亚特兰大梦想 vs 华盛顿奇异 2018-09-04 20:00:00,比赛ID：2597982,开始计算滚球ID:84949,滚球ID：84962,场节:Q4,初盘： 159.5,最大滚球分:184.5,最小滚球分:155.5
2018-09-15 15:07:28 - [ INFO ] 结果：【+】赢90.0000,买入【大】,按方向:反向操作,买入分数:166.5,当时总分:136,预计总分:150.86191,预计总分2:167.2953,时间:449,总分结果:172,大回报:0.900,小回报:0.760,Q4-全场得分:-0.0366,持续次数10,持续时间75,初率:0.0700,买入率:0.0331,全场率:0.0697 篮球俱乐部友谊赛 沙隆兰斯 vs 维勒布鲁克袋鼠篮球队 2018-09-04 13:00:00,比赛ID：2598836,开始计算滚球ID:83949,滚球ID：83972,场节:Q4,初盘： 166.5,最大滚球分:180.5,最小滚球分:163.5
2018-09-15 15:07:28 - [ INFO ] 结果：【+】赢83.0000,买入【大】,按方向:反向操作,再反转大于阀值0.0450,再反转大于消耗时间120，在0.4250,买入分数:175.5,当时总分:108,预计总分:150.2364,预计总分2:127.4346,时间:366,总分结果:181,大回报:0.830,小回报:0.830,Q4-全场得分:0.0623,持续次数10,持续时间136,初率:0.0717,买入率:0.1154,全场率:0.0531 篮球俱乐部友谊赛 俄斯特拉发 vs 奥洛穆茨科 2018-09-04 11:30:00,比赛ID：2598794,开始计算滚球ID:83532,滚球ID：83555,场节:Q4,初盘： 172,最大滚球分:185.5,最小滚球分:161.5
2018-09-1
		  */
		String[] gIds = { "2603295" };
		Map<String, List<BetRollInfo>> rollMaps = Maps.newConcurrentMap();
		Map<String, BetInfoDto> betInfoDtoMaps = Maps.newConcurrentMap();
		for (String gId : gIds) {
			List<BetRollInfo> rollList = baseDao.find("select * from bet_roll_info where gid in ("
					+ gId + ")   and se_now='Q4' order by gId,id ", BetRollInfo.class);

			BetInfo betInfo = baseDao.findOne("select * from bet_info where gid = '" + gId + "' ",
					BetInfo.class);
			BetInfoDto betInfoDto = new BetInfoDto();
			BeanUtils.copyProperties(betInfo, betInfoDto);
			BetCollector.CollectDataPool.bkRollList.add(betInfoDto);
			rollMaps.put(gId, rollList); 
		}
		Integer index = 0;
		while (true) {
			for (String gId : rollMaps.keySet()) {
				List<BetRollInfo> rollInfoList = rollMaps.get(gId);
				if (rollInfoList.size() > index) {
					for(BetInfoDto betInfoDto :BetCollector.CollectDataPool.bkRollList){
						if(betInfoDto.getGid().equals(gId)){
							betInfoDto.setLastUpdate(new Timestamp(System.currentTimeMillis()));
						}
					}
					BetRollInfo betRollInfo = rollInfoList.get(index);
					if (betRollInfo.getId() != null) {

						StringBuffer sb = new StringBuffer();
						sb.append("ID：").append(betRollInfo.getId());
						sb.append(",分数:" + betRollInfo.getRatio_rou_c()).append(",总分：")
								.append(StringUtils.leftPad(betRollInfo.getSc_total(), 3, "  "))
								.append(",").append(betRollInfo.getSe_now())
								.append(" 节剩余（秒）:"
										+ StringUtils.leftPad(betRollInfo.getT_count(), 3, " "))
								.append(",Q4-全场得分:")
								.append(fnum2.format(CalUtil.computeScoreSec4Quartz(betRollInfo)
										- CalUtil.computeScoreSec4Alltime(betRollInfo)))
								.append(" 每节进球（秒）："
										+ fnum2.format(CalUtil.computeScoreSec4Quartz(betRollInfo)))
								.append(" 全场进球（秒）：" + fnum2
										.format(CalUtil.computeScoreSec4Alltime(betRollInfo)))
								.append(" [Q1]：" + betRollInfo.getSc_Q1_total())
								.append(" [Q2]：" + betRollInfo.getSc_Q2_total())
								.append(" [Q3]：" + betRollInfo.getSc_Q3_total())
								.append(" [Q4]：" + betRollInfo.getSc_Q4_total());
						log.info(sb.toString());
						BetCollector.CollectDataPool.bkRollList.get(0)
								.setLastUpdate(new Timestamp(System.currentTimeMillis()));
						CollectDataPool.putRollDetail(betRollInfo);
					}
				}
			}
			index++;
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
