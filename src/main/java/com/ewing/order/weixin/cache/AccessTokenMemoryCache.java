package com.ewing.order.weixin.cache;

import com.ewing.order.core.redis.RedisManage;
import com.ewing.order.weixin.dto.AccessToken;
import com.ewing.order.weixin.dto.MemoryCache;

/**
 * 本地缓存AccessToken信息
 * 
 * @author 凡梦星尘(elkan1788@gmail.com)
 * @since 2.0
 */
public class AccessTokenMemoryCache implements MemoryCache<AccessToken> {

  	private final static String PREFIX_KEY ="ACCESS_TOKEN_";
    @Override
    public AccessToken get(String mpId) { 
        return RedisManage.getInstance().get(PREFIX_KEY+mpId);
    }

    @Override
    public void set(String mpId, AccessToken object) { 
    	RedisManage.getInstance().set(PREFIX_KEY+mpId, object);
    }

    @Override
    public void remove(String mpId) { 
    	RedisManage.getInstance().del(PREFIX_KEY+mpId);
    }

}
