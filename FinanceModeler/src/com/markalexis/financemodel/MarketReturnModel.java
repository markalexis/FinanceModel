package com.markalexis.financemodel;
/**
 * 
 */

/**
 * @author markalexis
 *
 */
public class MarketReturnModel {
	
	/**
	 * Constructor - no parameters
	 */
	public MarketReturnModel() {
		super();
		BuildDOWHistory();
	}
	//--------------------------------------------------------
	public double maxReturnPercent = 0.15;
	public double minreturnPercent = -0.05;
	public double[] dowHistory= new double[25]; 
	public double GetCalculatedReturn(){
		//returns a value based on random choice with the range Max - Min Pct
		double rn = Math.random();
		double result =0;
		
		//What is the range?
		double range = maxReturnPercent - minreturnPercent;
		//Calculate a result using the rn
		result = minreturnPercent +(rn*range);
		return result;
		
	}
	public double GetHistoricalReturn(){
		//returns a value based on random choice from DOW history table
		double rn = Math.random(); //Range is 0.0 to 1.0
		double result =0;
		
		int MaxIndex = dowHistory.length-1;
		
		//What is the range?
		double range = maxReturnPercent - minreturnPercent;
		//Calculate an index result using the rn
		int RandomIndex = (int)(rn*(double)MaxIndex);
		//Lookup Table....
		result = dowHistory[RandomIndex];
		return result;
		
	}
	private void BuildDOWHistory(){
		//Builds an array containing historical DOW market returns
		//The year is included just as a reference when updating table data
		//See http://www.forecast-chart.com/historical-dow-industrial.html
		//See http://pages.stern.nyu.edu/~adamodar/New_Home_Page/datafile/histretSP.html
		//
		
		dowHistory[0]= 0.305;//1991	30.5%
		dowHistory[1]= 0.076;//1992	7.6%
		dowHistory[2]= 0.101;//1993	10.1%
		dowHistory[3]= 0.013;//1994	1.3%
		dowHistory[4]= 0.376;//1995	37.6%
		dowHistory[5]= 0.230;//1996	23.0%
		dowHistory[6]= 0.334;//1997	33.4%
		dowHistory[7]= 0.286;//1998	28.6%
		dowHistory[8]= 0.210;//1999	21.0%
		dowHistory[9]= -0.091;//2000	-9.1%
		dowHistory[10]= -0.119;//2001	-11.9%
		dowHistory[11]= -0.221;//2002	-22.1%
		dowHistory[12]= 0.287;//2003	28.7%
		dowHistory[13]= 0.109;//2004	10.9%
		dowHistory[14]= 0.049;//2005	4.9%
		dowHistory[15]= 0.158;//2006	15.8%
		dowHistory[16]= 0.055;//2007	5.5%
		dowHistory[17]= -0.370;//2008	-37.0%
		dowHistory[18]= 0.265;//2009	26.5%
		dowHistory[19]= 0.151;//2010	15.1%
		dowHistory[20]= 0.021;//2011	2.1%
		dowHistory[21]= 0.160;//2012	16.0%
		dowHistory[22]= 0.324;//2013	32.4%
		dowHistory[23]= 0.0752;//2014
		dowHistory[24]= -0.0223;//2015
		
		
	}

}
