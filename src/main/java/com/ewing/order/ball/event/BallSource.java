package com.ewing.order.ball.event;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 *
 * @author tansonlam
 * @create 2018年7月30日
 */
public class BallSource {
	private static Logger log = LoggerFactory.getLogger(BetInfoListener.class);
	private Map<String, BetInfoListener> listeners = Maps.newConcurrentMap();

	private static Map<String, BallSource> ballSourceInstances = Maps.newConcurrentMap();

	public static BallSource getBKCurrent() {
		return getInstance("BKCurrent");
	}

	public static BallSource getBKRoll() {
		return getInstance("BKRoll");
	}

	public static BallSource getFTCurrent() {
		return getInstance("FTCurrent");
	}

	public static BallSource getFTRoll() {
		return getInstance("FTRoll");
	}

	private static BallSource getInstance(String gtype) {
		BallSource instance = ballSourceInstances.get(gtype);
		if (instance == null) {
			instance = new BallSource();
			instance = ballSourceInstances.putIfAbsent(gtype, instance);
			if (instance == null)
				return ballSourceInstances.get(gtype);
		}
		return instance;
	}

	private BallSource() {

	}

	/**
	 * 注册事件监听器
	 * 
	 * @param listener
	 */
	public void addBallListener(BetInfoListener eventListener) {
		BetInfoListener existListener = listeners.get(eventListener.getAccount());
		if (existListener != null) {
			existListener.stopListener();
		}
		listeners.put(eventListener.getAccount(), eventListener);
	}

	public BetInfoListener getBetInfoListener(String account){
		return  listeners.get(account);
	}
	
	public Map<String, BetInfoListener> getAllListener(){
		return listeners;
	} 
	
	public void stopBallListener(String account) {
		BetInfoListener existListener = listeners.get(account);
		if (existListener != null) {
			existListener.stopListener();
		}
		listeners.remove(account);
	} 
}
