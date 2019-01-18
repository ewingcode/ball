package com.ewing.order.ball.event;

import java.util.Map;
import java.util.WeakHashMap;

import com.ewing.order.ball.util.CalUtil;
import com.ewing.order.busi.ball.ddl.BetRollInfo;
import com.google.common.collect.Maps;

/**
 *
 * @author tansonlam
 * @create 2019年1月15日 
 */
public class InRateCache{
	static Map<Integer,Float> score4QuartzCache = Maps.newConcurrentMap();
	static Map<Integer,Float> scoreAllQuartzCache =  Maps.newConcurrentMap();
	static Map<Integer,Float> scoreBefore4QuartzCache =  Maps.newConcurrentMap();
	public static void clearAll(){
		score4QuartzCache.clear();
		scoreAllQuartzCache.clear();
		scoreAllQuartzCache.clear();
	}
	public static Float computeScoreSec4Quartz(BetRollInfo betRollInfo){
		Float score4Quartz = score4QuartzCache.get(betRollInfo.getId());
		if(score4Quartz!=null)
			return score4Quartz;
		score4Quartz = CalUtil.computeScoreSec4Quartz(betRollInfo);
		score4QuartzCache.put(betRollInfo.getId(), score4Quartz);
		return score4Quartz;
	}
	
	public static Float computeScoreSec4Alltime(BetRollInfo betRollInfo){
		Float scoreAllQuartz = scoreAllQuartzCache.get(betRollInfo.getId());
		if(scoreAllQuartz!=null)
			return scoreAllQuartz;
		scoreAllQuartz = CalUtil.computeScoreSec4Alltime(betRollInfo);
		scoreAllQuartzCache.put(betRollInfo.getId(), scoreAllQuartz);
		return scoreAllQuartz;
	}
	
	public static Float computeScoreSecBefore4Q(BetRollInfo betRollInfo){
		Float scoreBefore4Quartz = scoreBefore4QuartzCache.get(betRollInfo.getId());
		if(scoreBefore4Quartz!=null)
			return scoreBefore4Quartz;
		scoreBefore4Quartz = CalUtil.computeScoreSecBefore4Q(betRollInfo);
		scoreBefore4QuartzCache.put(betRollInfo.getId(), scoreBefore4Quartz);
		return scoreBefore4Quartz;
	}
	 
}
