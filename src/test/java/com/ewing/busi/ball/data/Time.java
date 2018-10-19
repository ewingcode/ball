package com.ewing.busi.ball.data;

import java.util.Date;

import com.ewing.order.ball.util.RequestTool;

public class Time {
	public static void main(String[] args) {
		Long t = System.currentTimeMillis();
		System.out.println(new Date(t)); 
		System.out.println(new Date(RequestTool.add6Sec(t)));
		
		System.out.println(new Date(1539738518172l)); 
		System.out.println(new Date(1539738523373l));
		
	}
}	
