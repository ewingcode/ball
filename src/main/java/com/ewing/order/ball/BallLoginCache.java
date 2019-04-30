package com.ewing.order.ball;

import java.util.Map;

import com.ewing.order.ball.login.LoginResp;
import com.google.common.collect.Maps;

/**
 *
 * @author tansonlam
 * @create 2019年4月30日 
 */
public class BallLoginCache {
private static Map<String, LoginResp> ballAccountLoginCache = Maps.newConcurrentMap();
	
	private static Map<String, String> account2BallAccountCache = Maps.newConcurrentMap();
	
	public static void account2BallAccountCache(String account,String ballAccount){
		account2BallAccountCache.put(account, ballAccount);
	}
	
	public static String getBallAccount4Cache(String account){
		return account2BallAccountCache.get(account);
	}
	
	public static void removeLoginResp(String account) {
		String ballAccount = account2BallAccountCache.get(account);
		if(ballAccount==null){
			return;
		}
		ballAccountLoginCache.remove(ballAccount);
	}
	
	public static LoginResp getLoginResp(String account) {
		String ballAccount = account2BallAccountCache.get(account);
		if(ballAccount==null){
			return null;
		}
		return ballAccountLoginCache.get(ballAccount);
	}
	
	public static void cacheLoginResp(String account,LoginResp loginResp) throws Exception{
		String ballAccount = account2BallAccountCache.get(account);
		if(ballAccount==null){
			throw new Exception("没有找到匹配的球网登录账号");
		}
		ballAccountLoginCache.put(ballAccount, loginResp);
	}
}
