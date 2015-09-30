package com.markalexis.financemodel;

import java.util.Locale;

/*
 * Author: M Alexis
 * Data structure to hold data for each year of the finance model
 */
public class YearData {
	private int year = 1900;
	private double startBalance = 0;
	private String startBalanceFormatted = "";
	public double endBalance =0;
	public int age = 0;
	public boolean retired = false;
	public boolean incomeGoalMet = true;
	private double marketGains =0;		//In dollars
	private String marketGainsFormatted = "";
	//public double netGains = 0;
	public double marketGainPercent =0;
	public double stableGainPercent = 0;
	public double percentInvestedMarket = 0;
	public double inflationRate = .025;
	public double incomePension= 0;
	public double incomeSSSelf = 0;
	public double incomeSSSpouse = 0;
	private double incomeGoalWithInflation =0;
	private String incomeGoalWithInflationFormatted = "";
	
	private double incomeTotal =0;				//Total of all retirement income sources
	private String incomeTotalFormatted = "";
	public Double IncomeShortfall = 0.0;
	private double savingsWithdraw =0;
	private String savingsWithdrawFormatted = "";
	
	public double savingsContribution =0;
	public String  toString(){
		//Mainly for debug, provide a string record for console output
		//Show the results
		String YearSummary = "";
		
		if(this.retired){
			//Shows an overall result for each year, blank if not retired
			if(this.incomeGoalMet){
				YearSummary = "Success!";
			}
			else{
				YearSummary = "Shortfall!, " + this.ToCurrency(this.IncomeShortfall);
			}
		}

		String StatusMsg = this.getYear() + " " + 
				" ["+ this.age +"] " +
				" SGainPct: " + this.ToPct(this.marketGainPercent) +
				" Gains: "+ this.ToCurrency(this.getMarketGains()) +
				" Bal: " + this.ToCurrency(this.endBalance) +
				" IncGoal: " + this.ToCurrency(this.getIncomeGoalWithInflation()) + 
				" IncTotal: " + this.ToCurrency(this.getIncomeTotal()) +
				" Draw: " + this.ToCurrency(this.getSavingsWithdraw()) +
				" " + YearSummary;
				;
		return StatusMsg;
	}
	private String ToCurrency(double Value)
	{
		String result = "$" + String.format(Locale.US, "%,10.0f", Value);
		return result;
	}
	private  String ToPct(double Value)
	{
		String result = "%" + String.format(Locale.US, "%,6.2f", Value*100);
		return result;
	}
	//TODO: Method to dump YearDataTo tabbed screen line
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public double getStartBalance() {
		return startBalance;
	}
	public void setStartBalance(double startBalance) {
		this.startBalance = startBalance;
		this.startBalanceFormatted = this.ToCurrency(this.startBalance);
	}
	public String getStartBalanceFormatted() {
		return startBalanceFormatted;
	}
	private void setStartBalanceFormatted(String startBalanceFormatted) {
		this.startBalanceFormatted = startBalanceFormatted;
	}
	public double getMarketGains() {
		return marketGains;
	}
	public void setMarketGains(double marketGains) {
		this.marketGains = marketGains;
		this.marketGainsFormatted = this.ToCurrency(this.marketGains)+ "(" + this.ToPct(this.marketGainPercent)+")";
	}
	@SuppressWarnings("unused")
	public String getMarketGainsFormatted() {
		return marketGainsFormatted;
	}
	@SuppressWarnings("unused")
	public void setMarketGainsFormatted(String marketGainsFormatted) {
		this.marketGainsFormatted = marketGainsFormatted;
	}
	public double getIncomeGoalWithInflation() {
		return incomeGoalWithInflation;
	}
	public void setIncomeGoalWithInflation(double incomeGoalWithInflation) {
		this.incomeGoalWithInflation = incomeGoalWithInflation;
		this.incomeGoalWithInflationFormatted = this.ToCurrency(this.incomeGoalWithInflation);
	}
	public String getIncomeGoalWithInflationFormatted() {
		return incomeGoalWithInflationFormatted;
	}
	public void setIncomeGoalWithInflationFormatted(String incomeGoalWithInflationFormatted) {
		this.incomeGoalWithInflationFormatted = incomeGoalWithInflationFormatted;
	}
	public double getIncomeTotal() {
		return incomeTotal;
	}
	public void setIncomeTotal(double incomeTotal) {
		this.incomeTotal = incomeTotal;
		this.incomeTotalFormatted = this.ToCurrency(this.incomeTotal);
	}
	public String getIncomeTotalFormatted() {
		return incomeTotalFormatted;
	}
	public void setIncomeTotalFormatted(String incomeTotalFormatted) {
		this.incomeTotalFormatted = incomeTotalFormatted;
	}
	public double getSavingsWithdraw() {
		return savingsWithdraw;
	}
	public void setSavingsWithdraw(double savingsWithdraw) {
		this.savingsWithdraw = savingsWithdraw;
		this.savingsWithdrawFormatted = this.ToCurrency(this.savingsWithdraw);
	}
	public String getSavingsWithdrawFormatted() {
		return savingsWithdrawFormatted;
	}
	public void setSavingsWithdrawFormatted(String savingsWithdrawFormatted) {
		this.savingsWithdrawFormatted = savingsWithdrawFormatted;
	}
	
}
