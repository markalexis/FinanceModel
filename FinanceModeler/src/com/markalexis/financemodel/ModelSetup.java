package com.markalexis.financemodel;
/**
 * 
 */

//import com.markalexis.financemodel.*;

/**
 * @author markalexis
 *
 */
public class ModelSetup {
	
	//Allows selection of the model type that predicts market gains each year
	public enum MarketModelType  {Fixed, Range, Historical};
	
	public MarketModelType Model = MarketModelType.Range;
	
	//Calendar Year Related
	public int startYear = 2015; 
	public double percentInvestedInMarketPost = .50;
	public double percentinvestedInMarketPre = 1.00;
	
	//Age Related Parameters
	public int ageStart = 54;
	public int ageRetire = 65;
	public int ageSSSelf = 67;
	public int ageSSSpouse = 67;
	
	//Dollar amount items - in thousands.  Income amounts are annual
	public double startBalance = 1600000;	//Dollar amounts in dollars
	public double pensionIncome = 5500;
	public double socialSecurityIncomeSelf = 32746;
	public double socialSecurityIncomeSpouse = 20544;
	public double incomeDesired = 100000;
	public double inflationRateStatic = 0.025;
	public double savingsContributions = 40000;//401 k savings pre-retirement
	
	
	//Misc, rates
	public double maxPercentWithdraw = 0.04;
	public double marketReturnsStatic = 0.06;	//DOW, Etc.
	public Double stableReturnsStatic = 0.04;	//Bonds, Monry market, etc
	
	//TODO: break down SS income by age;
	
	
	
	
}
