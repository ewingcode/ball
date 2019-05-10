package com.ewing.order.ball;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Maps;

/**
 *
 * @author tansonlam
 * @create 2019年5月10日
 */
public class BallInfoCache {
	/**
	 * 滚球信息  key:gid value：消失记录多少次
	 */
	private final static Map<String, AtomicInteger> rollGIds = Maps.newConcurrentMap();
	
	private final static Integer maxMissingTime = 60;
	
	public static void addGid(String gid){
		if(rollGIds.containsKey(gid)){
			return;
		}
		rollGIds.put(gid, new AtomicInteger(0));
	}
	
	public static Boolean addMissingTime(String gid){
		if(!rollGIds.containsKey(gid)){
			return false;
		}
		int missTime = rollGIds.get(gid).incrementAndGet();
		if(missTime>=maxMissingTime){
			rollGIds.remove(gid);
			return false;
		}else{
			return true;
		}
	}
	 
}
