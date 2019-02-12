package com.ewing.busi.ball.betway;

/**
 *
 * @author tansonlam
 * @create 2019年2月1日 
 */
public class ContinueBet {

	private Float targetMoney = 250f;
	
	private Float betMoney = 0f;
	
	private Float lostTotal = 0f;
	
	private Float maxBetEachMatch = 2000f;
	
	private Float rate = 0.8f;
	public Boolean count(){
		betMoney = lostTotal/rate+targetMoney;
 
		System.out.println("下注金额:"+betMoney);
		if(betMoney>=maxBetEachMatch)
				return false;
		lostTotal +=targetMoney/rate  ;
		return true;
	}
	
	public static void main(String[] args) {
		ContinueBet continueBet = new ContinueBet();
		while(continueBet.count()){
			
		}
	}
	
}
