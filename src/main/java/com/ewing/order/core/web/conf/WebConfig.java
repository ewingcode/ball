package com.ewing.order.core.web.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ewing.order.core.app.interceptor.TokenValidateInterceptor;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	static final String ORIGINS[] = new String[] { "GET", "POST", "PUT", "DELETE" };

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		set4PrjInnerResource(registry);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowCredentials(true)
				.allowedMethods(ORIGINS).maxAge(3600);
	}

	private void set4PrjInnerResource(ResourceHandlerRegistry registry) {
		// 设置项目相关资源
		registry.addResourceHandler("/*").addResourceLocations("classpath:/static/");
	}

	/**
	 * 配置拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TokenValidateInterceptor()).addPathPatterns("/busi/**");
	}

}
