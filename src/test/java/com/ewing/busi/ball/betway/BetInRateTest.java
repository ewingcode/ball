/**
 * 
 */
package com.ewing.busi.ball.betway;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

/**
 * @author Administrator
 *
 */
public class BetInRateTest {

	private static Integer MAX_MATCH_NUM=10;
	private static Float winRate = 0.6f;
	private static Float ballIncomeRate = 1.83f;
	public static float computeRate() {
		return (1+1/(ballIncomeRate-1))*winRate-(1/(ballIncomeRate-1));
	}
	private static  void appendByNum(List<Integer> matchResults, Integer result, Integer num) {
		
		for (int i = 0; i < num; i++) {
			matchResults.add(result);
		}
	}

	public static List<Integer> randomMatchResult(Float winRate) { 
		List<Integer> tempList = Lists.newArrayList();
		List<Integer> randomResults = Lists.newArrayList();
		appendByNum(tempList,1,(Float.valueOf(MAX_MATCH_NUM*winRate)).intValue());
		appendByNum(tempList,-1,(Float.valueOf(MAX_MATCH_NUM*(1-winRate))).intValue());
	
		for(int i =0;i<MAX_MATCH_NUM;i++) {
			System.out.println("比赛胜率比例："+tempList);
			Random r = new Random();
			Integer rIndex = r.nextInt(tempList.size()); 
			
			Integer result = tempList.get(rIndex);
			tempList.remove(rIndex.intValue());
			randomResults.add(result);
			System.out.println("rIndex："+rIndex+",result:"+result);
		}
		System.out.println("比赛结果："+randomResults);
		return randomResults;
	}
	
	public static void compute() {
		List<Integer> randomResults = randomMatchResult(winRate);
	
		float rate = computeRate();
		System.out.println("下注率:"+rate);
		float pool = 1000f;
		int matchNum = 0;
		for(int i =0;i<MAX_MATCH_NUM;i++) {
			float betMoney = pool * rate;
			Integer result = randomResults.get(matchNum);
			if(result>0) {
				pool = pool + betMoney;
			}else {
				pool = pool - betMoney;
			}
			
			System.out.println("场次:" + (++matchNum) + ",下注：" + betMoney + ",资金池:" + pool);
		}
	}

	public static void main(String[] args) {
		compute();
	}
}
