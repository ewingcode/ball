package com.ewing.order.ball.event;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.ball.event.strategy.BKBasicBetStrategy;
import com.ewing.order.ball.event.strategy.BKRollSetReStrategy;
import com.ewing.order.ball.event.strategy.FTBasicBetStrategy;
import com.ewing.order.common.exception.BusiException;

/**
 * 投注策略中心
 *
 * @author tansonlam
 * @create 2018年8月2日
 */
public class BetStrategyRegistCenter {
	private static Logger log = LoggerFactory.getLogger(BetStrategyRegistCenter.class);
	public final static Map<String, Class<? extends BetStrategy>> betStrategies = new HashMap<>();

	static {
		addBetStrategy(FTBasicBetStrategy.class);
		addBetStrategy(BKBasicBetStrategy.class);
		addBetStrategy(BKRollSetReStrategy.class);
	}

	public static void addBetStrategy(Class<? extends BetStrategy> clazz) {
		betStrategies.put(clazz.getSimpleName(), clazz);
	}

	public static BetStrategy newBetStrategy(String betStrategyClazzName) {
		Class<? extends BetStrategy> clazz = betStrategies.get(betStrategyClazzName);
		if (clazz == null)
			throw new BusiException("没有对应的投注规则处理器：" + betStrategyClazzName);
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			log.error("投注规则处理器初始化失败：" + betStrategyClazzName);
			return null;
		}
	}
}
