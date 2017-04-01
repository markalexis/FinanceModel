package com.markalexis.financemodel;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

//import FinanceModeler.MarketReturnModel;
//import FinanceModeler.ModelSetup;
//import FinanceModeler.YearData;

/**
 * 
 */

/**
 * @author markalexis
 *
 */
public class ModelUtility {
	public ModelSetup MS = new ModelSetup();
	//public int YearCount = 30;		//How many years to model?
	public YearData modelData[]= null;	
	//Observable List for use with parent table control
	public ObservableList<YearData> modelDataList = FXCollections.observableArrayList();
	//--------------------------------------------------------
	//Use this observable list for charting in parent FX apps
	public Series<String, Double> balanceSeries = new Series<String, Double>();
	public Series<String, Double> incomeSeries = new Series<String, Double>();
	public Series<String, Double> incomeGoalSeries = new Series<String, Double>();
	public Series<String, Double> savingsDrawSeries = new Series<String, Double>();
	
	//public double[] balance;
	//public int[] years;
	//public ObservableList<Data<String, Double>> balanceDataList  = new FXCollections.observableArrayList();
	public ObservableList<Series<String,Double>> balanceDataList = FXCollections.observableArrayList();
	public ObservableList<Series<String,Double>> incomeDataList = FXCollections.observableArrayList();
	
	//--------------------------------------------------------------------------------------------------
	//Use the utility
	//ModelUtility MU = new ModelUtility();
	public MarketReturnModel MRM = new MarketReturnModel();
	
	
	public String ToCurrency(double Value)
	{
		String result = "$" + String.format(Locale.US, "%,10.0f", Value);
		return result;
	}
	public String ToPct(double Value)
	{
		String result = "% " + String.format(Locale.US, "%,6.2f", Value*100);
		return result;
	}
	public YearData[] runMultiYearModel(int yearCount){
		//Runs a financial model over multiple years
		//TODO: return an array of YearData
		YearData modelData[]= new YearData[yearCount];
		//Clear Chart Data collections before running a new model
		modelDataList.clear();
		//balanceDataList.clear();
		
		//balance = new double[yearCount];
		balanceSeries.getData().clear();
		incomeSeries.getData().clear();
		incomeSeries.setName("Income");
		incomeGoalSeries.getData().clear();
		incomeGoalSeries.setName("Goal");
		savingsDrawSeries.getData().clear();
		savingsDrawSeries.setName("Savings Draw");
		
		//balanceDataList.addAll(balanceSeries);
		//balanceDataList.clear();
		
		//Now iterate over all years
				for (int i =0; i<yearCount;i++){
					
					//Create a new YearData class each year of the model creates a new instance
					YearData ThisYear = new YearData();
					//ThisYear = (YearData)ModelData[i];
					
					//Special conditions for the first year of the model
					if (i==0){
						// Set up Year 0, the starting year
						ThisYear.setStartBalance(MS.startBalance);
						ThisYear.setYear(MS.startYear);
						ThisYear.setAge((double) MS.ageStart);
						ThisYear.setIncomeGoalWithInflation(MS.incomeDesired);
					}
					else{
						//All other years - Initialize
						//Get the start balance from the prior year
						YearData LastYear = (YearData)modelData[i-1];
						ThisYear.setStartBalance(LastYear.endBalance);
						ThisYear.setYear(LastYear.getYear()+1);
						ThisYear.setAge(LastYear.getAge() +1);
						ThisYear.setIncomeGoalWithInflation(LastYear.getIncomeGoalWithInflation()* (1.00+MS.inflationRateStatic));
					}
					
					//Set up the age related flags for this year
					if(ThisYear.getAge()>=MS.ageRetire){
						ThisYear.retired = true;
						ThisYear.incomePension = MS.pensionIncome;
						ThisYear.savingsContribution =0;
					}
					else{
						ThisYear.retired = false;
						ThisYear.incomePension = 0;
						ThisYear.setSavingsWithdraw(0);
						ThisYear.savingsContribution = MS.savingsContributions;
					}
					//Social security Income - two sources at different times
					if(ThisYear.getAge()>=MS.ageSSSelf){
						//TODO: Add inflation adjustments to SS
						ThisYear.incomeSSSelf = MS.socialSecurityIncomeSelf;
					}
					else{
						ThisYear.incomeSSSelf = 0;
					}
					//Social security Income - two sources at different times
					if(ThisYear.getAge()>=MS.ageSSSpouse){
						//TODO: Add inflation adjustments to SS
						ThisYear.incomeSSSpouse = MS.socialSecurityIncomeSpouse;
					}
					else{
						ThisYear.incomeSSSpouse = 0;
					}
					//Once all income sources have been identified, determine what must be withdrawn from savings
					//What is the allowed savings withrawal?
					double DrawLimit = MS.maxPercentWithdraw*ThisYear.getStartBalance();
					
					//Create a proposed savings draw, the compare this with a max draw limit
					double ProposedSavingsDraw = ThisYear.getIncomeGoalWithInflation() 
							- ThisYear.incomePension
							- ThisYear.incomeSSSelf
							- ThisYear.incomeSSSpouse;
					
					if(ThisYear.retired == true){
						//Now apply savings draw limits to protect principle
						if (ProposedSavingsDraw>DrawLimit){
							ThisYear.setSavingsWithdraw(DrawLimit);
						}
						else{
							ThisYear.setSavingsWithdraw(ProposedSavingsDraw);
						}
						
					}
					ThisYear.setIncomeTotal(ThisYear.incomePension 
							+ ThisYear.incomeSSSelf 
							+ ThisYear.incomeSSSpouse
							+ ThisYear.getSavingsWithdraw());
					

		        	//Did we meet the goal for the year?
					ThisYear.IncomeShortfall = ThisYear.getIncomeGoalWithInflation() -  ThisYear.getIncomeTotal();
					if(ThisYear.getIncomeTotal() >= ThisYear.getIncomeGoalWithInflation()){
						ThisYear.incomeGoalMet = true;
					}
					else{
						ThisYear.incomeGoalMet = false;
					}
						
					
					
					//Start Building gains for each year
					switch (MS.Model) {
		            	case Fixed:  
		            		ThisYear.marketGainPercent = MS.marketReturnsStatic;
		                    break;
		            	case Historical:  
		            		ThisYear.marketGainPercent = MRM.GetHistoricalReturn();
		                    break;
		            	case Range:  
		            		ThisYear.marketGainPercent = MRM.GetCalculatedReturn();
		            		break;       
		            	default: 
		            		ThisYear.marketGainPercent = 0.01;
		                    break;
					}
					
					//Set up gains for stable investments
					ThisYear.stableGainPercent = MS.stableReturnsStatic;
					
					Double CurrentYearMarketReturnFunds = 0.0;
					Double CurrentYearStabileReturnFunds = 0.0;
					
					if (ThisYear.retired){
						CurrentYearMarketReturnFunds = ThisYear.getStartBalance() * MS.percentInvestedInMarketPost *ThisYear.marketGainPercent;
						CurrentYearStabileReturnFunds = ThisYear.getStartBalance() * (1-MS.percentInvestedInMarketPost)*ThisYear.stableGainPercent;
					}
					else
					{
						CurrentYearMarketReturnFunds = ThisYear.getStartBalance()*ThisYear.marketGainPercent;
						CurrentYearStabileReturnFunds = 0.0;
					}
					
					ThisYear.setMarketGains(CurrentYearMarketReturnFunds + CurrentYearStabileReturnFunds);
					
					//Total all gains, losses, income and withdrawals to get the end balance for the year
					//Assumes pension, SS income will be used for income in the year
		
					
					ThisYear.endBalance = ThisYear.getStartBalance() 
							+ ThisYear.getMarketGains()
							+ ThisYear.savingsContribution 
							-ThisYear.getSavingsWithdraw();
					
					//-------------------Reporting ----------------------------
					
					modelData[i]= ThisYear;
					//Add to the ObservableList collection
					modelDataList.add(ThisYear);
					//For FX charting
					
					Double y = ThisYear.getAge();
					String yS = y.toString(y);
					XYChart.Data newData = new Data(yS, ThisYear.endBalance);
					XYChart.Data incomeData = new Data(yS, ThisYear.getIncomeTotal());
					XYChart.Data incomeGoalData = new Data(yS, ThisYear.getIncomeGoalWithInflation() );
					XYChart.Data savingsDrawData = new Data(yS, ThisYear.getSavingsWithdraw() );
					
					//balanceSeries.getData().add(new XYChart.Data(Integer.toString(y), ThisYear.endBalance));
					balanceSeries.getData().add(newData);
					incomeSeries.getData().add(incomeData);
					incomeGoalSeries.getData().add(incomeGoalData);
					savingsDrawSeries.getData().add(savingsDrawData);
					
					
					//balanceDataList.addAll(newData);
					
				}
				//Update the Chart Observable List
				//balanceDataList.addAll(balanceSeries);
				//incomeDataList.addAll(incomeSeries);
				
				
				//Return the results
				return modelData;
				
				//5. Store each simulated result
				
				//6. Save results
			}
	
}
