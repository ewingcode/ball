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
		/*
		 * 结果：【+】赢83.0000,买入【大】,按方向:反向操作,买入分数:155.5,总分结果:156,Q4-全场得分:-0.0380,
		 * 持续次数10,持续时间110,初率:0.0717,买入率:0.0247,全场率:0.0627,大回报:0.830,小回报:0.830
		 * 篮球俱乐部友谊赛 亚洛瓦 vs 卡雷西 2018-09-10
		 * 10:00:00,比赛ID：2602133,开始计算滚球ID:117033,滚球ID：117042,场节:Q4,时间:438,初盘：
		 * 173.5,最大滚球分:175.5,最小滚球分:147.5
		 * 结果：【+】赢80.0000,买入【大】,按方向:反向操作,买入分数:153.5,总分结果:162,Q4-全场得分:-0.0328,
		 * 持续次数10,持续时间70,初率:0.0617,买入率:0.0310,全场率:0.0638,大回报:0.800,小回报:0.800
		 * 日本B篮球联赛初期杯 福岛火绊 vs 岩手大牛 2018-09-07
		 * 03:00:00,比赛ID：2599529,开始计算滚球ID:90921,滚球ID：90932,场节:Q4,时间:471,初盘：
		 * 149.5,最大滚球分:158.5,最小滚球分:129.5
		 * 结果：【+】赢76.0000,买入【大】,按方向:反向操作,买入分数:166.5,总分结果:170,Q4-全场得分:-0.0321,
		 * 持续次数10,持续时间77,初率:0.0667,买入率:0.0380,全场率:0.0701,大回报:0.760,小回报:0.900
		 * 篮球俱乐部友谊赛 布林迪西 vs 莫尔纳 2018-09-06
		 * 14:30:00,比赛ID：2599361,开始计算滚球ID:90412,滚球ID：90422,场节:Q4,时间:416,初盘：
		 * 159.5,最大滚球分:188.0,最小滚球分:158.5
		 * 结果：【-】输100.0000,买入【大】,按方向:反向操作,买入分数:165.5,总分结果:153,Q4-全场得分:-0.0406,
		 * 持续次数12,持续时间77,初率:0.0683,买入率:0.0278,全场率:0.0684,大回报:0.960,小回报:0.700
		 * 篮球俱乐部友谊赛 尼维斯 vs 利马帕 2018-09-05
		 * 12:00:00,比赛ID：2599109,开始计算滚球ID:86557,滚球ID：86600,场节:Q4,时间:456,初盘：
		 * 162.5,最大滚球分:181.5,最小滚球分:162.5
		 * 结果：【+】赢90.0000,买入【大】,按方向:反向操作,买入分数:165.5,总分结果:167,Q4-全场得分:-0.0290,
		 * 持续次数10,持续时间137,初率:0.0667,买入率:0.0402,全场率:0.0692,大回报:0.900,小回报:0.760
		 * WNBA季后赛 亚特兰大梦想 vs 华盛顿奇异 2018-09-04
		 * 20:00:00,比赛ID：2597982,开始计算滚球ID:84949,滚球ID：84962,场节:Q4,时间:376,初盘：
		 * 159.5,最大滚球分:184.5,最小滚球分:155.5
		 * 结果：【+】赢90.0000,买入【大】,按方向:反向操作,买入分数:166.5,总分结果:172,Q4-全场得分:-0.0366,
		 * 持续次数10,持续时间75,初率:0.0700,买入率:0.0331,全场率:0.0697,大回报:0.900,小回报:0.760
		 * 篮球俱乐部友谊赛 沙隆兰斯 vs 维勒布鲁克袋鼠篮球队 2018-09-04
		 * 13:00:00,比赛ID：2598836,开始计算滚球ID:83949,滚球ID：83972,场节:Q4,时间:449,初盘：
		 * 166.5,最大滚球分:180.5,最小滚球分:163.5
		 * 结果：【+】赢83.0000,买入【大】,按方向:反向操作,再反转大于阀值0.0450,买入分数:175.5,总分结果:181,Q4-
		 * 全场得分:0.0623,持续次数10,持续时间136,初率:0.0717,买入率:0.1154,全场率:0.0531,大回报:0.830,
		 * 小回报:0.830 篮球俱乐部友谊赛 俄斯特拉发 vs 奥洛穆茨科 2018-09-04
		 * 11:30:00,比赛ID：2598794,开始计算滚球ID:83532,滚球ID：83555,场节:Q4,时间:366,初盘：
		 * 172,最大滚球分:185.5,最小滚球分:161.5
		 * 结果：【-】输100.0000,买入【大】,按方向:反向操作,买入分数:154.5,总分结果:152,Q4-全场得分:-0.0275,
		 * 持续次数10,持续时间104,初率:0.0717,买入率:0.0347,全场率:0.0622,大回报:0.830,小回报:0.830
		 * WNBA季后赛 凤凰城水星 vs 西雅图暴风 2018-08-31
		 * 22:00:00,比赛ID：2596316,开始计算滚球ID:71618,滚球ID：71651,场节:Q4,时间:456,初盘：
		 * 172.5,最大滚球分:176.5,最小滚球分:150.5
		 * 结果：【+】赢80.0000,买入【大】,按方向:反向操作,买入分数:131.5,总分结果:139,Q4-全场得分:-0.0364,
		 * 持续次数10,持续时间108,初率:0.0600,买入率:0.0173,全场率:0.0537,大回报:0.800,小回报:0.800
		 * FIBA南美洲女子锦标赛(在哥伦比亚) 巴拉圭(女) vs 智利(女) 2018-08-30
		 * 14:15:00,比赛ID：2596127,开始计算滚球ID:63840,滚球ID：63854,场节:Q4,时间:427,初盘：
		 * 142.5,最大滚球分:150.5,最小滚球分:131.5
		 * 结果：【+】赢96.0000,买入【大】,按方向:反向操作,再反转大于阀值0.0450,买入分数:169.5,总分结果:170,Q4-
		 * 全场得分:0.0473,持续次数10,持续时间105,初率:0.0633,买入率:0.1179,全场率:0.0706,大回报:0.960,
		 * 小回报:0.700 篮球俱乐部友谊赛 萨拉托夫 vs 萨马拉 2018-08-30
		 * 07:00:00,比赛ID：2596617,开始计算滚球ID:62691,滚球ID：62707,场节:Q4,时间:388,初盘：
		 * 153.5,最大滚球分:172.5,最小滚球分:150.5
		 * 结果：【-】输100.0000,买入【大】,按方向:反向操作,买入分数:138.5,总分结果:126,Q4-全场得分:-0.0376,
		 * 持续次数10,持续时间143,初率:0.0633,买入率:0.0191,全场率:0.0567,大回报:0.700,小回报:0.960
		 * 篮球俱乐部友谊赛 迪纳恩 vs 奥尔希 2018-08-29
		 * 13:30:00,比赛ID：2596442,开始计算滚球ID:61863,滚球ID：61894,场节:Q4,时间:391,初盘：
		 * 153.5,最大滚球分:155.5,最小滚球分:134.5
		 * 结果：【+】赢83.0000,买入【大】,按方向:反向操作,再反转大于阀值0.0450,买入分数:164.5,总分结果:177,Q4-
		 * 全场得分:0.0503,持续次数10,持续时间89,初率:0.0683,买入率:0.1184,全场率:0.0681,大回报:0.830,
		 * 小回报:0.830 篮球俱乐部友谊赛 里雄莱锡安马卡比 vs 比尔舒华夏普尔 2018-08-29
		 * 10:00:00,比赛ID：2596526,开始计算滚球ID:60905,滚球ID：60932,场节:Q4,时间:448,初盘：
		 * 165.5,最大滚球分:172.5,最小滚球分:152.5
		 * 结果：【+】赢70.0000,买入【小】,按方向:反向操作,买入分数:151.5,总分结果:135,Q4-全场得分:0.0362,
		 * 持续次数10,持续时间89,初率:0.0700,买入率:0.0976,全场率:0.0614,大回报:0.960,小回报:0.700
		 * 篮球俱乐部友谊赛 BC科林 vs 英吉夫赫拉德茨 2018-08-28
		 * 11:30:00,比赛ID：2596001,开始计算滚球ID:59757,滚球ID：59773,场节:Q4,时间:477,初盘：
		 * 168.5,最大滚球分:174.5,最小滚球分:145.5
		 * 结果：【+】赢88.0000,买入【大】,按方向:反向操作,买入分数:157.5,总分结果:163,Q4-全场得分:-0.0343,
		 * 持续次数10,持续时间100,初率:0.0717,买入率:0.0295,全场率:0.0638,大回报:0.880,小回报:0.780
		 * 篮球俱乐部友谊赛 BK奥帕瓦 vs 日利纳 2018-08-28
		 * 11:30:00,比赛ID：2596106,开始计算滚球ID:59807,滚球ID：59825,场节:Q4,时间:363,初盘：
		 * 172.5,最大滚球分:179.5,最小滚球分:154.5
		 * 结果：【+】赢80.0000,买入【大】,按方向:反向操作,买入分数:156.5,总分结果:161,Q4-全场得分:-0.0292,
		 * 持续次数10,持续时间80,初率:0.0650,买入率:0.0356,全场率:0.0648,大回报:0.800,小回报:0.800
		 * 亚运会2018男子篮球(在印尼) 中国 vs 印尼 2018-08-27
		 * 07:30:00,比赛ID：2595434,开始计算滚球ID:58251,滚球ID：58260,场节:Q4,时间:347,初盘：
		 * 157.5,最大滚球分:170.5,最小滚球分:155.5
		 * 结果：【-】输100.0000,买入【大】,按方向:反向操作,买入分数:138.5,总分结果:134,Q4-全场得分:-0.0394,
		 * 持续次数10,持续时间122,初率:0.0633,买入率:0.0136,全场率:0.0530,大回报:0.700,小回报:0.960
		 * 篮球俱乐部友谊赛 BC奥斯坦德 vs 莱切斯特骑士 2018-08-26
		 * 08:00:00,比赛ID：2595497,开始计算滚球ID:55866,滚球ID：55876,场节:Q4,时间:380,初盘：
		 * 153.5,最大滚球分:154.5,最小滚球分:134.5 2018-09-13 16:
		 */
		String[] gIds = { "2599529", "2602133" };
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
