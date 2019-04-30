package com.ewing.order.ball;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.aliyuncs.utils.StringUtils;
import com.ewing.order.ball.login.LoginResp;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 *
 * @author tansonlam
 * @create 2019年4月30日 
 */
public class BallLoginCache {
private static Map<String, LoginResp> ballAccountLoginCache = Maps.newConcurrentMap();
	
	private static Map<String, String> account2BallAccountCache = Maps.newConcurrentMap();
	
	/**
	 * 根据相同球网账号查找有关联的账号
	 * @param account
	 * @return
	 */
	public static List<String> getAccountByBallAccount(String account){
		 String ballAccount = getBallAccount4Cache(account);
		 if(StringUtils.isEmpty(ballAccount))
			 return Lists.newArrayList();
		 List<String> relAccountList = Lists.newArrayList();
		 for(String key : account2BallAccountCache.keySet()){
			 String value = account2BallAccountCache.get(key);
			 if(value.equals(ballAccount)){ 
				 relAccountList.add(value);
			 } 
		 }
		 return relAccountList;
	}
	public static void account2BallAccountCache(String account,String ballAccount){
		account2BallAccountCache.put(account, ballAccount);
	}
	
	public static String getBallAccount4Cache(String account){
		return account2BallAccountCache.get(account);
	}
	
	public static void removeLoginResp(String ballAccount) { 
		ballAccountLoginCache.remove(ballAccount);
	}
	
	public static LoginResp getLoginResp(String account) {
		String ballAccount = account2BallAccountCache.get(account);
		if(ballAccount==null){
			return null;
		}
		return ballAccountLoginCache.get(ballAccount);
	}
	
	public static LoginResp getLoginRespByBallAccount(String ballAccount) { 
		return ballAccountLoginCache.get(ballAccount);
	}
	
	public static void cacheLoginResp(String ballAccount,LoginResp loginResp) throws Exception{ 
		ballAccountLoginCache.put(ballAccount, loginResp);
	}
}
