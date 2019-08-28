/**
 * 
 */
package com.ewing.busi.ball.betway;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

/**
 * @author Administrator
 *
 */
public class BetInRateTest {

	private static Integer MAX_MATCH_NUM=20;
	private static Double winRate = 0.3d;
	private static Double ballIncomeRate = 1.7d;
	

	static DecimalFormat fnum3 = new DecimalFormat("##0.00");
	public static Double computeRate() {
		Double rate =  (1+1/(ballIncomeRate-1))*winRate-(1/(ballIncomeRate-1));
		 return Math.abs(Double.valueOf(fnum3.format(rate)));
	}
	private static  void appendByNum(List<Integer> matchResults, Integer result, Integer num) {
		
		for (int i = 0; i < num; i++) {
			matchResults.add(result);
		}
	}

	public static List<Integer> randomMatchResult(Double winRate) { 
		List<Integer> tempList = Lists.newArrayList();
		List<Integer> randomResults = Lists.newArrayList();
		appendByNum(tempList,1,(Double.valueOf(MAX_MATCH_NUM*winRate)).intValue());
		appendByNum(tempList,-1,(Double.valueOf(MAX_MATCH_NUM*(1-winRate))).intValue());
	
		for(int i =0;i<MAX_MATCH_NUM;i++) {
		//System.out.println("比赛胜率比例："+tempList);
			Random r = new Random();
			Integer rIndex = r.nextInt(tempList.size()); 
			
			Integer result = tempList.get(rIndex);
			tempList.remove(rIndex.intValue());
			randomResults.add(result);
			//System.out.println("rIndex："+rIndex+",result:"+result);
		}
		System.out.println("比赛结果："+randomResults);
		return randomResults;
	}
	
	public static void compute() {
		List<Integer> randomResults = randomMatchResult(winRate);
	
		Double rate = 0.1d;
		System.out.println("下注池资金率:"+rate+",胜率:"+winRate);
		Double pool = 1000d;
		int matchNum = 0;
		for(int i =0;i<MAX_MATCH_NUM;i++) {
			Double betMoney = pool * rate;
			Integer result = randomResults.get(matchNum);
			if(result>0) {
				pool = pool + betMoney*(ballIncomeRate);
			}else {
				pool = pool - betMoney;
			}
			
			System.out.println("场次:" + (++matchNum) + ",结果："+(result==1?"赢":"输")+"，下注：" + betMoney + ",资金池:" + pool);
		}
	}

	public static void main(String[] args) {
		compute();
	}
}
