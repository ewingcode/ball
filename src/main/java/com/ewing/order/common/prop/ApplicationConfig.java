package com.ewing.order.common.prop;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ewing.order.core.redis.CacheConfig;

@Configuration
@EnableConfigurationProperties({ OAuthProp.class,CacheConfig.class })
public class ApplicationConfig {

}
