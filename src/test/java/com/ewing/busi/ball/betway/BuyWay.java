package com.ewing.busi.ball.betway;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.ball.dto.BetInfoDto;
import com.ewing.order.ball.event.InRateCache;
import com.ewing.order.ball.util.CalUtil;
import com.ewing.order.busi.ball.ddl.BetRollInfo;
import com.google.common.collect.Lists;

/**
 *
 * @author tansonlam
 * @create 2019年1月16日 
 */
public class BuyWay {
	private static Logger log = LoggerFactory.getLogger(CalData.class);

	static DecimalFormat fnum = new DecimalFormat("##0.00");
	static DecimalFormat fnum2 = new DecimalFormat("##0.0000");
	static DecimalFormat fnum3 = new DecimalFormat("##0.000");
	private String id;
	private Float interval;
	private Integer minHighScoreTime;
	private int rollSmall_suc;
	private int rollSmall_fail;
	private String seNow;
	float smallWinMoney;
	float smallBuyMoneyEach = 100f;
	boolean showWinDetail = false;
	private Integer highScoreCostTime;
	List<StringBuffer> smallBuyList = Lists.newArrayList();
	private String buySide;
	private String side;
	private Boolean desc;
	private Float maxintervalPercent;
	private Integer leftTime;
	private int way = 2;
	private Float intervalInPercent;
	private Boolean isAll;

	public BuyWay(String id,Float interval, Integer minHighScoreTime, Integer highScoreCostTime,
			String buySide, String seNow, Boolean desc, Float maxintervalPercent,
			Integer leftTime, Float intervalInPercent, Boolean isAll) {
		this.id = id;
		this.interval = interval;
		this.minHighScoreTime = minHighScoreTime;
		this.highScoreCostTime = highScoreCostTime;
		this.buySide = buySide;
		this.side = buySide;
		this.seNow = seNow;
		this.desc = desc;
		this.maxintervalPercent = maxintervalPercent;
		this.leftTime = leftTime;
		this.intervalInPercent = intervalInPercent;
		this.isAll = isAll;
	}
	
	public Boolean isEqual(BuyWay buyWay2){
		return this.id.equals(buyWay2.id);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void combine(BuyWay buyWay2){
		if(buyWay2==null)
			return;
		this.rollSmall_fail += buyWay2.rollSmall_fail;
		this.rollSmall_suc += buyWay2.rollSmall_suc;
		this.smallWinMoney += buyWay2.smallWinMoney;
		this.smallBuyList.addAll(buyWay2.smallBuyList);
	}

	public void compute(List<BetInfoDto> betInfoDtoList) {
		for (BetInfoDto betInfo : betInfoDtoList) {
			/*
			 * if (betInfo.getMinRatioRou() == null) { continue; }
			 */
			try {
				checkBuy(betInfo);
			} catch (Exception e) {
				log.error("滚球:" + betInfo.getGid() + ",错误：" + e.getMessage(), e);
			}
		}

	}

	public int totalMatch() {
		return rollSmall_suc + rollSmall_fail;
	}

	public Float smallWinMoney() {
		return smallWinMoney;
	}

	public Float sucRate() {
		return (rollSmall_suc / ((rollSmall_suc + rollSmall_fail) * 1f));
	}

	public Float totalBetMoney() {
		return smallBuyMoneyEach * (rollSmall_suc + rollSmall_fail) * 1f;
	}

	public Float betMoneyRate() {
		Float totalBetMoney = totalBetMoney();
		if (totalBetMoney == null || totalBetMoney == 0f)
			return 0f;
		return smallWinMoney / totalBetMoney;
	}

	private Boolean isAutoBuy() {
		return buySide.equals("AUTO");
	}

	public void checkBuy(BetInfoDto betInfo) {

		BetRollInfo buySmall = null;
		List<BetRollInfo> list = CalData.rollMap.get(betInfo.getGid());
		if (CollectionUtils.isEmpty(list))
			return;
		int highScoreTime = 0;
		int tmpHighScoreCostTime = 0;
		BetRollInfo previousBetRollInfo = null;
		float inter = 0f;
		side = "";
		String tmpSide = "";
		BetRollInfo beginBetRollInfo = null;
		for (BetRollInfo betRollInfo : list) {
			
			Integer tempLeftTime = null;
			if (this.leftTime != null) {
				tempLeftTime = this.leftTime;
				if (CalUtil.isNba(betRollInfo.getLeague())) {
					tempLeftTime = this.leftTime + 100;
				}
			}
			if (buySmall == null) {

				if (previousBetRollInfo != null
						&& previousBetRollInfo.isSameRatioOU(betRollInfo)) {
					continue;
				}

				if (seNow != null && !seNow.equals(betRollInfo.getSe_now())) {
					continue;
				}
				if (tempLeftTime != null && !StringUtils.isEmpty(betRollInfo.getT_count())
						&& CalUtil.getFixTcount(betRollInfo.getT_count()) > tempLeftTime) {
					continue;
				}
				float scoreEveryQuartz = InRateCache.computeScoreSec4Quartz(betRollInfo);
				float scoreAllQuartz = CalData.computeScoreSec4Alltime(betRollInfo, isAll);
				if (scoreEveryQuartz == 0f || scoreAllQuartz == 0f)
					continue;
				inter = Math.abs(scoreEveryQuartz - scoreAllQuartz);
				if (way == 2 && intervalInPercent != null)
					interval = scoreAllQuartz * intervalInPercent;
				if (interval == null)
					continue;
				if (isAutoBuy()) {
					if (scoreEveryQuartz > scoreAllQuartz && inter >= interval) {
						tmpSide = "H";
					} else if (scoreEveryQuartz < scoreAllQuartz && inter >= interval) {
						tmpSide = "C";
					} else {
						tmpSide = "";
					}
				}
				if (!StringUtils.isEmpty(tmpSide) && !StringUtils.isEmpty(side)
						&& !tmpSide.equals(side)) {
					highScoreTime = 0;
					tmpHighScoreCostTime = 0;
					beginBetRollInfo = null;
				}
				if (!StringUtils.isEmpty(tmpSide)) {
					if (!tmpSide.equals(side)) {
						highScoreTime = 0;
					}
					side = tmpSide;
				}

				if (side.equalsIgnoreCase("H")) {
					if (scoreEveryQuartz - scoreAllQuartz >= interval) {
						highScoreTime++;
						if (beginBetRollInfo == null)
							beginBetRollInfo = betRollInfo;
						else
							tmpHighScoreCostTime = CalUtil
									.getFixTcount(beginBetRollInfo.getT_count())
									- CalUtil.getFixTcount(betRollInfo.getT_count());
					} else {
						highScoreTime = 0;
						tmpHighScoreCostTime = 0;
						beginBetRollInfo = null;
					}
				} else if (side.equalsIgnoreCase("C")) {
					if (scoreAllQuartz - scoreEveryQuartz >= interval) {
						highScoreTime++;
						if (beginBetRollInfo == null)
							beginBetRollInfo = betRollInfo;
						else
							tmpHighScoreCostTime = CalUtil
									.getFixTcount(beginBetRollInfo.getT_count())
									- CalUtil.getFixTcount(betRollInfo.getT_count());
					} else {
						highScoreTime = 0;
						beginBetRollInfo = null;
						tmpHighScoreCostTime = 0;
					}
				}

				if (minHighScoreTime != null && highScoreCostTime != null) {
					if (highScoreTime >= minHighScoreTime
							&& tmpHighScoreCostTime >= highScoreCostTime) {
						buySmall = betRollInfo;
						break;
					}
				} else {

					if (minHighScoreTime != null && highScoreTime == minHighScoreTime) {
						buySmall = betRollInfo;
						break;
					}

					if (highScoreCostTime != null
							&& tmpHighScoreCostTime >= highScoreCostTime) {
						buySmall = betRollInfo;
						break;
					}

				}
				previousBetRollInfo = betRollInfo;
			}

		}

		if (buySmall != null && betInfo.getSc_total() != null
				&& buySmall.getRatio_rou_c() != null) {

			String operateName = desc ? "反向操作" : "正向操作";
			boolean isreverse = false;
			float scoreEveryQuartz = InRateCache.computeScoreSec4Quartz(buySmall);
			float scoreAllQuartz = CalData.computeScoreSec4Alltime(buySmall, isAll);
			if (maxintervalPercent == null && way == 1) {
				if (scoreAllQuartz / scoreEveryQuartz > 2f
						|| scoreEveryQuartz / scoreAllQuartz > 2f) {
					isreverse = true;
					operateName += ",大于2倍反转";
				}
			}

			if (!desc) {
				if (side.equals("H")) {
					side = "C";
				} else if (side.equals("C")) {
					side = "H";
				}
			}
			if ((maxintervalPercent != null
					&& inter >= (interval + maxintervalPercent * interval))) {
				operateName += (maxintervalPercent != null
						&& inter >= (interval + maxintervalPercent * interval))
								? ",再反转大于阀值"
										+ fnum2.format(interval + maxintervalPercent * interval)
								: "";
				if (side.equals("H")) {
					side = "C";
				} else if (side.equals("C")) {
					side = "H";
				}
			}

			if (way == 3) {
				boolean hasLowRate = false;
				if (Float.valueOf(buySmall.getIor_ROUC()) >= 0.9f
						&& Float.valueOf(buySmall.getIor_ROUH()) <= 0.8f) {
					operateName += ",低回报率买入小";
					side = "H";
					hasLowRate = true;
				}
				if (Float.valueOf(buySmall.getIor_ROUC()) <= 0.8f
						&& Float.valueOf(buySmall.getIor_ROUH()) >= 0.9f) {
					operateName += ",低回报率买入大";
					side = "C";
					hasLowRate = true;
				}
				if (!hasLowRate)
					return;
			}
			StringBuffer sb = new StringBuffer();
			// side = buySmall.getRatio_rou_c()
			// >expectLeftScore(buySmall)?"C":"H";
			// side =
			// Float.valueOf(buySmall.getIor_ROUH())>Float.valueOf(buySmall.getIor_ROUC())?"C":"H";
			String ratio_ou = side.equals("H") ? buySmall.getIor_ROUH()
					: buySmall.getIor_ROUC();
			float winMoney = Float.valueOf(ratio_ou) * smallBuyMoneyEach;
			boolean isWin = false;
			if (side.equals("H")) {
				isWin = Float.valueOf(betInfo.getSc_total()) < buySmall.getRatio_rou_c();
			} else if (side.equals("C")) {
				isWin = Float.valueOf(betInfo.getSc_total()) > buySmall.getRatio_rou_c();
			}
			if (isWin) {
				sb.append("结果：【+】赢" + fnum2.format(winMoney));
				rollSmall_suc++;
				smallWinMoney += winMoney;
			} else {
				sb.append("结果：【-】输" + fnum2.format(smallBuyMoneyEach));
				rollSmall_fail++;
				smallWinMoney -= smallBuyMoneyEach;
			}
			sb.append(",比赛ID：").append(buySmall.getGid()).append(",买入")
					.append(side.equals("H") ? "【小】" : "【大】").append(",按方向:")
					.append(operateName).append(",买入分数:").append(buySmall.getRatio_rou_c())
					.append(",总分结果:").append(betInfo.getSc_total())
//					.append(",即买入率:")
//					.append(fnum2.format(scoreEveryQuartz))
//					.append(",即全场率:")
//					.append(fnum2.format(computeScoreSec4Alltime(buySmall, isAll)))
//					.append(",完全场率:").append(fnum2.format(CalUtil.computeWholeRate(betInfo)))
					.append(",预计总分:").append(CalData.expectLeftScore(buySmall)).append(",预计总分2:")
					.append(CalData.expectLeftScore2(buySmall)).append(",预计总分3:")
					.append(CalData.expectLeftScore3(buySmall)).append(",时间:")
					.append(buySmall.getT_count()).append(",当时总分:")
					.append(buySmall.getSc_total()).append(",间隔分率:").append(interval)
					.append(",4-全场得分:")
					.append(fnum2.format(scoreEveryQuartz
							- scoreAllQuartz))
					.append(",持续次数").append(highScoreTime).append(",持续时间")
					.append(tmpHighScoreCostTime).append(",大回报:")
					.append(buySmall.getIor_ROUC()).append(",小回报:")
					.append(buySmall.getIor_ROUH()).append(" " + betInfo.getLeague())
					.append(" " + betInfo.getTeam_h()).append(" vs ")
					.append(betInfo.getTeam_c()).append(" " + betInfo.getDatetime());

			if (beginBetRollInfo != null)
				sb.append(",开始计算滚球ID:" + beginBetRollInfo.getId());
			sb.append(",滚球ID：").append(buySmall.getId()).append(",场节:")
					.append(buySmall.getSe_now()).append(",初盘：" + betInfo.getRatio_o())
					.append(",最大滚球分:").append(betInfo.getMaxRatioRou()).append(",最小滚球分:")
					.append(betInfo.getMinRatioRou());
			if (showWinDetail) {
				smallBuyList.add(sb);
			}
		}

	}

	public void printResult() {
		log.info("========================================================");
		log.info("方案ID:" + this.id + ",滚球买入" + buySide + "，得分率差" + interval + ",得分率差比例:"
				+ intervalInPercent + ",最大阀值比例:" + maxintervalPercent + ",场数："
				+ (rollSmall_suc + rollSmall_fail) + ",出现次数:" + minHighScoreTime + ",持续时间:"
				+ highScoreCostTime + ",剩余时间:" + leftTime + ",是否反向买入:" + desc + ",是否全场得分："
				+ isAll);
		log.info("滚球买入" + buySide + "，得分率差" + interval + ",比率："
				+ fnum.format((rollSmall_suc / ((rollSmall_suc + rollSmall_fail) * 1f)) * 100)
				+ "%," + ",赢场次:" + rollSmall_suc + ",输场次" + rollSmall_fail + ",下注:"
				+ (smallBuyMoneyEach * (rollSmall_suc + rollSmall_fail)) + ",金额:"
				+ smallWinMoney);
		if (showWinDetail) {
			for (StringBuffer smallBuy : smallBuyList) {
				log.info(smallBuy.toString());
			}
		}
	}

	public void shortPrintResult() {
		log.info("方案ID:" + this.id + ",滚球买入" + buySide + "，得分率差" + interval + ",得分率差比例:"
				+ intervalInPercent + ",最大阀值比例:" + maxintervalPercent + ",场数："
				+ (rollSmall_suc + rollSmall_fail) + ",出现次数:" + minHighScoreTime + ",持续时间:"
				+ highScoreCostTime + ",剩余时间:" + leftTime + ",是否反向买入:" + desc + ",是否全场得分："
				+ isAll+",比率："
				+ fnum.format((rollSmall_suc / ((rollSmall_suc + rollSmall_fail) * 1f)) * 100)
				+ "%," + ",赢场次:" + rollSmall_suc + ",输场次" + rollSmall_fail + ",下注:"
				+ (smallBuyMoneyEach * (rollSmall_suc + rollSmall_fail)) + ",金额:"
				+ smallWinMoney);
	}

	public Float getInterval() {
		return interval;
	}

	public void setInterval(Float interval) {
		this.interval = interval;
	}

	public Integer getMinHighScoreTime() {
		return minHighScoreTime;
	}

	public void setMinHighScoreTime(Integer minHighScoreTime) {
		this.minHighScoreTime = minHighScoreTime;
	}

	public int getRollSmall_suc() {
		return rollSmall_suc;
	}

	public void setRollSmall_suc(int rollSmall_suc) {
		this.rollSmall_suc = rollSmall_suc;
	}

	public int getRollSmall_fail() {
		return rollSmall_fail;
	}

	public void setRollSmall_fail(int rollSmall_fail) {
		this.rollSmall_fail = rollSmall_fail;
	}

	public String getSeNow() {
		return seNow;
	}

	public void setSeNow(String seNow) {
		this.seNow = seNow;
	}

	public float getSmallWinMoney() {
		return smallWinMoney;
	}

	public void setSmallWinMoney(float smallWinMoney) {
		this.smallWinMoney = smallWinMoney;
	}

	public float getSmallBuyMoneyEach() {
		return smallBuyMoneyEach;
	}

	public void setSmallBuyMoneyEach(float smallBuyMoneyEach) {
		this.smallBuyMoneyEach = smallBuyMoneyEach;
	}

	public boolean isShowWinDetail() {
		return showWinDetail;
	}

	public void setShowWinDetail(boolean showWinDetail) {
		this.showWinDetail = showWinDetail;
	}

	public Integer getHighScoreCostTime() {
		return highScoreCostTime;
	}

	public void setHighScoreCostTime(Integer highScoreCostTime) {
		this.highScoreCostTime = highScoreCostTime;
	}

	public List<StringBuffer> getSmallBuyList() {
		return smallBuyList;
	}

	public void setSmallBuyList(List<StringBuffer> smallBuyList) {
		this.smallBuyList = smallBuyList;
	}

	public String getBuySide() {
		return buySide;
	}

	public void setBuySide(String buySide) {
		this.buySide = buySide;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public Boolean getDesc() {
		return desc;
	}

	public void setDesc(Boolean desc) {
		this.desc = desc;
	}

	public Float getMaxintervalPercent() {
		return maxintervalPercent;
	}

	public void setMaxintervalPercent(Float maxintervalPercent) {
		this.maxintervalPercent = maxintervalPercent;
	}

	public Integer getLeftTime() {
		return leftTime;
	}

	public void setLeftTime(Integer leftTime) {
		this.leftTime = leftTime;
	}

	public int getWay() {
		return way;
	}

	public void setWay(int way) {
		this.way = way;
	}

	public Float getIntervalInPercent() {
		return intervalInPercent;
	}

	public void setIntervalInPercent(Float intervalInPercent) {
		this.intervalInPercent = intervalInPercent;
	}

	public Boolean getIsAll() {
		return isAll;
	}

	public void setIsAll(Boolean isAll) {
		this.isAll = isAll;
	}
}