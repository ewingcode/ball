package com.ewing.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

/**
 * 服务启动类
 * 
 * @author tanson lam
 * @create 2016年9月2日
 */
@SpringBootApplication
@ComponentScan
@ServletComponentScan 
@EnableCaching
public class Door extends SpringBootServletInitializer {
  static{
	  setLog4j2Path();
  }
	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(Door.class);
	} 
	
	private static void setLog4j2Path(){
		String dir = System.getProperty("user.dir");
		System.setProperty("catalina.home", dir);
	}
	
	public static void main(String[] args) {
		//setLog4j2Path();
		SpringApplication.run(Door.class, args);
	}
}
