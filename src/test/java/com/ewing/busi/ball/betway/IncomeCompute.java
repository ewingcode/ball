package com.ewing.busi.ball.betway;

public class IncomeCompute {
	//旧税费每月支出
	private float oldMonthTax ;
	//每月收入
	private float moneyIncome; 
	//免税起征点
	private float taxBeginMoney;// = 5000f;
	//保险和公积金
	private float insuranceMoney;//  ;
	//专项扣除
	private float freeMoney;// = 4000f;
	//每月收入用于计算税费
	private float monthTaxMoney;
	//全年总收入用于计算税费
	private float yearTaxMoney;
	//总税费
	private float totalTax;
	public IncomeCompute(float oldMonthTax,float moneyIncome,float taxBeginMoney,float insuranceMoney, float freeMoney){
		this.oldMonthTax =oldMonthTax;
		this.moneyIncome = moneyIncome;
		this.taxBeginMoney= taxBeginMoney;
		this.insuranceMoney =insuranceMoney;
		this.freeMoney = freeMoney;
		this.monthTaxMoney =  moneyIncome - taxBeginMoney-insuranceMoney - freeMoney;
	}
	public Float computeOldTax(){
		return oldMonthTax*12;
	}
	public Float computeNewTax(){ 
		for(int i =1;i<=12;i++){
			yearTaxMoney += monthTaxMoney;
			YearTax yearTax = YearTax.matchTaxLevel(yearTaxMoney);
			float monthTax = monthTaxMoney*yearTax.getFaxPercent()-yearTax.getSubtract();
			totalTax+=monthTax;
			System.out.println("第"+i+"月-全年累计可扣税收入:"+yearTaxMoney+",税："+monthTax);
		}
		//System.out.println("总税："+totalTax);
		return totalTax;
	}
	public static void main(String[] args) {
		IncomeCompute incomeCompute = new IncomeCompute(1111111f,1111111f,5000f,5100f,4000f);
		Float newTax = incomeCompute.computeNewTax();
		Float oldTax = incomeCompute.computeOldTax();
		System.out.println("新年税："+newTax+",旧年税:"+oldTax+",差额:"+(Math.abs(oldTax-newTax)));
	}
	
	enum YearTax{
		ONE(0f,36000f,0.03f,0f),
		TWO(36000f,144000f,0.1f,210f),
		THREE(144000f,300000f,0.2f,1410f),
		FOUR(300000f,420000f,0.25f,2660f),
		FIVE(420000f,660000f,0.3f,4410f),
		SIX(660000f,960000f,0.35f,7160f),
		SERVEN(960000f,10000000f,0.45f,15160f);
		private Float min;
		private Float max;
		private Float faxPercent;
		private Float subtract;
		 YearTax(Float min,Float max,Float faxPercent,Float subtract){
			this.min = min;
			this.max = max;
			this.faxPercent =faxPercent;
			this.subtract =subtract;
		}
		 
		public Float getMin() {
			return min;
		}

		public Float getMax() {
			return max;
		}

		public Float getFaxPercent() {
			return faxPercent;
		}

		public Float getSubtract() {
			return subtract;
		}

		public static YearTax matchTaxLevel(Float yearIncome){
			for(YearTax yearTax : YearTax.values()){
				if(yearIncome >= yearTax.min && yearIncome< yearTax.max){
					return yearTax;
				}
			}
			throw new IllegalArgumentException("没有对应的年收入税项,收入："+yearIncome);
		}
	}
}
