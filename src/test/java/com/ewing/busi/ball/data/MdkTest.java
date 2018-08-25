package com.ewing.busi.ball.data;

/**
 *
 * @author tansonlam
 * @create 2018年8月23日
 */
public class MdkTest {
	private static Double calvinTemperature = 273.1d;
	private static Double e = 2.71828d;
	//private static Double e = 2d;

	private static Double mdkValue(double t) {
		return Math.pow(e, -10000 / (calvinTemperature + t));
	}

	public static double log(double value, double base) {
		return Math.log(value) / Math.log(base);
	}


	static void compute1(){ 
        double value = (2 * mdkValue(20d) + 4 * mdkValue(2d) + 1 * mdkValue(25d) )/ 7d;
		double s1 = -10000
				/ log(value, e);
		System.out.println("MKT:"+s1);
		System.out.println("MKT C:"+(s1 - calvinTemperature)); 
	}
	
	static void compute2(){ 
        double value = (2 * mdkValue(20d) + 4 * mdkValue(2d) + 1 * mdkValue(25d)+0.5*mdkValue(75d) )/ 7.5d;
		double s1 = -10000
				/ log(value, e);
		System.out.println("MKT:"+s1);
		System.out.println("MKT C:"+(s1 - calvinTemperature)); 
	}
	
	static void compute3(){ 
        double epow = mdkValue(25d);
        double value = 52*epow/52;
		double s1 = -10000
				/ log( value, e);
		System.out.println("MKT:"+s1);
		System.out.println("MKT C:"+(s1 - calvinTemperature)); 
	}
	public static void main(String[] args) {
		compute1();
		compute2();
		compute3(); 
	}
}
