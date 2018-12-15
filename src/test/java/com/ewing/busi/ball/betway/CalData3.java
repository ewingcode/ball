package com.ewing.busi.ball.betway;

public class CalData3 {
	public static void main(String[] args) {
		/*
		 * 【-】输100.0000,比赛ID：2728147,买入【大】,按方向:反向操作,买入分数:161.5,总分结果:149,时间:418,当时总分:130,
		 * 间隔分率:0.05,Q4-全场得分:-0.0271,持续次数5,持续时间123,初率:0.0000,买入率:0.0385,全场率:0.0890,预计总分:
		 * 146.093,预计总分2:157.4208,大回报:0.880,小回报:0.780 日本B1篮球联赛 东芝斗士 vs 横滨海盗 2018-12-08
		 * 05:05:00,开始计算滚球ID:538781,滚球ID：538797,场节:Q4,初盘：大 153.5,最大滚球分:null,最小滚球分:null
		 */
		float q4rate = 0.0385f;
		float q3rate = 0.0890f;
		Integer imTotal = 130;
		Integer leftTime = 418;
		computeTotal(q4rate, q3rate, imTotal, leftTime, false);
	}

	public static float computeTotal(float q4rate, float q3rate, Integer imTotal, Integer leftTime,
			Boolean isBigCount) {
		Integer eachCount = isBigCount ? 720 : 600;
		float q4cost = eachCount - leftTime;
		Integer totalCost = eachCount * 4 - leftTime;
		float expectRate = q3rate * ((eachCount * 3) / (totalCost * 1f)) + q4rate * (q4cost / (totalCost * 1f));

		float expectTotal = imTotal + expectRate * leftTime;
		System.out.println(expectRate + "," + expectTotal);
		return expectTotal;
	}

}
