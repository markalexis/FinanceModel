package com.markalexis.financemodel;
/**
 * 
 */

//import FinanceModeler.ModelUtility;
//import FinanceModeler.YearData;

/**
 * @author markalexis
 *
 *	Simple Monte Carlo Finance modeler for evaluating 401k savings
 *
 */
//TODO: push function down into Model utility and have the iterations return an array or YearData()
// 

public class RunModel {

	/**
	 * @param args
	 */

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String openingMessage = "Basic Financial Modeler ";
		String versionMessage = "Version 1.0 ";
		/*ModelSetup MS = new ModelSetup();
		int YearCount = 30;		//How many years to model?
		YearData modelData[]= new YearData[YearCount];	
		
		MarketReturnModel MRM = new MarketReturnModel();
		*/
		//Use the utility
		ModelUtility MU = new ModelUtility();
		
		//-------------------------------------------------------------------------------------------------	
		System.out.println(openingMessage+versionMessage);

		//1. Open up a an  XML properties file to be used for storing values for the model
		//For now, use the class ModelSetup and its defaults
		
		
		//2. Load up array databases for DOW performance History, Inflation History to be used in the model
		
		//3. Ask a series of questions about how to run the model?  Skip this if we put a UI on it
		
		//4. Run a a model
		
		YearData[] thisModel = MU.runMultiYearModel(30);
		
		for(int i=0;i<thisModel.length;i++){
			//Show the results
			String YearSummary = "";
			YearData ThisYear = thisModel[i];
			if(ThisYear.retired){
				//Shows an overall result for each year, blank if not retired
				if(ThisYear.incomeGoalMet){
					YearSummary = "Success!";
				}
				else{
					YearSummary = "Shortfall!, " + MU.ToCurrency(ThisYear.IncomeShortfall);
				}
			}

			String StatusMsg = ThisYear.getYear() + " " + 
					" ["+ ThisYear.age +"] " +
					" SGainPct: " + MU.ToPct(ThisYear.marketGainPercent) +
					" Gains: "+ MU.ToCurrency(ThisYear.getMarketGains()) +
					" Bal: " + MU.ToCurrency(ThisYear.endBalance) +
					" IncGoal: " + MU.ToCurrency(ThisYear.getIncomeGoalWithInflation()) + 
					" IncTotal: " + MU.ToCurrency(ThisYear.getIncomeTotal()) +
					" Draw: " + MU.ToCurrency(ThisYear.getSavingsWithdraw()) +
					" " + YearSummary;
					;
			System.out.println(StatusMsg);
		}
		
		//5. Store each simulated result
		
		//6. Save results
	}

}
