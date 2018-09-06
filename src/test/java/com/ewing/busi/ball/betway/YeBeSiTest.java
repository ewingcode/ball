package com.ewing.busi.ball.betway;

public class YeBeSiTest {

	public static void testBall(){
		Integer box1_a_white=8;
		Integer box1_b_red = 5;
		Integer box2_c_white=6;
		Integer box2_d_red = 7;
		Float per = (box1_a_white/((box1_a_white+box1_b_red)*1f)) 
				*((box2_c_white+1)/((box2_c_white+box2_d_red+1)*1f))
				+(box1_b_red/((box1_a_white+box1_b_red)*1f))
				*(box2_c_white/((box2_c_white+box2_d_red+1)*1f));
		System.out.println(per);
	}
	
	public static void main(String[] args) {
		testBall();
	}
}
