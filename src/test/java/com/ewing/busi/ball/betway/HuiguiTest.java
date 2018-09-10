package com.ewing.busi.ball.betway;

 
public class HuiguiTest {
   public static double cal(double x){
	   return 5.0967d-0.0749*x;
   }
   public static void main(String[] args) {
	 Double[] waters = {102.714d,95.154d,114.364d,120.170d,129.393d};
	 for(Double w:waters){
		 System.out.println("水："+w+"，下榻:"+cal(w));
	 }
   }
}
