package com.markalexis.financemodel;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 */

//import com.markalexis.financemodel.*;

/**
 * @author markalexis
 *
 */
public class ModelSetup {
	
	//New Approach to ease editing and storing parameters - use a Map structure and K,V pairs
	//TODO: Save and retrieve from file, XML
	
	//public Map<String,Double> modelMap = new HashMap<String,Double>();
	//public Map<String,Double> modelMap =  new HashMap<String,Double>();
	
	//This wrapping allows forms to observe and update the modelMap
	// see http://docs.oracle.com/javafx/2/collections/jfxpub-collections.htm
	//public ObservableMap<String, Double> obsModelMap = FXCollections.observableMap(modelMap);
	//public ObservableList<Map> obsModelMap = ( FXCollections.observableArrayList(modelMap));
	
	//
	
	//public ObservableMap<String,Double> obsMap = new ObservableMap(modelMap); 
	//Use this map for settings file logic - key/value pairs saved to text file.
	public Map<String, String> settingsMap = new TreeMap<String, String>();
	
	
	//Allows selection of the model type that predicts market gains each year
	public enum MarketModelType  {Fixed, Range, Historical};
	
	public MarketModelType Model = MarketModelType.Range;
	
	//Calendar Year Related
	public Integer startYear = 2016; 
	
	//Percentages
	public Double percentInvestedInMarketPost = .50;
	public Double percentinvestedInMarketPre = 1.00;
	
	//Age Related Parameters
	public Integer ageStart = 55;
	public Integer ageRetire = 64;
	public Integer ageSSSelf = 67;
	public Integer ageSSSpouse = 67;
	
	//Dollar amount items - in USD.  Income amounts are annual
	public Double startBalance = 1600000.0;	//Dollar amounts in dollars
	public Double pensionIncome = 5500.0;
	public Double socialSecurityIncomeSelf = 32746.0;
	public Double socialSecurityIncomeSpouse = 20544.0;
	public Double incomeDesired = 100000.0;
	public Double inflationRateStatic = 0.025; //Inflation is historically averaged 2.5%
	public Double savingsContributions = 42000.0;//401 k savings per year pre-retirement
	
	
	//Misc, rates
	public Double maxPercentWithdraw = 0.04;
	public Double marketReturnsStatic = 0.06;	//DOW, Etc.
	public Double stableReturnsStatic = 0.04;	//Bonds, Money market, etc
	
	//TODO: break down SS income by age;
	//-------------------Constructor here:-----------------------------------
	public ModelSetup(){
		//Initialize the parameter map
		
		//Age Related Parameters
		//modelMap.put("AgeStart", 53.0);
		//modelMap.put("AgeRetire", 65.0);
		
		//Dollar amount items - in USD.  Income amounts are annual
		//modelMap.put("StartBalance", 1600000.0);
		
	}
	public void setSettingFromKeyValuePair(String sKey, String sVal){
		/*Sets appropriate property from a key/value pair, matching key with
			the property, converting to the appropriate 
		 	data type
		 * */
		
		//TODO: Add all value key pairs to the parser - MDA 
		switch (sKey.trim()) {
		  
			case "startYear":
	        	startYear = Integer.valueOf(sVal);
	        	break;
			case "ageStart":
		        ageStart = Integer.valueOf(sVal);
		        break;
			case "ageRetire":
		        ageRetire = Integer.valueOf(sVal);
		        break;
			case "startBalance":
			  	startBalance = Double.valueOf(sVal);
		        break;
		        
		}
		
		
	}
	public void readModelSettingsFromFile(File SettingsFile) throws IOException{
		//Reads key=value pairs from file, places them in a Map for eventual conversion
		final int lhs = 0;
        final int rhs = 1;

        //TreeMap<String, String> settingsMap = new TreeMap<String, String>();
        BufferedReader  bfr = new BufferedReader(new FileReader(SettingsFile));

        String line;
        while ((line = bfr.readLine()) != null) {
            if (!line.startsWith("#") && !line.isEmpty()) {
                String[] pair = line.trim().split("=");
                settingsMap.put(pair[lhs].trim(), pair[rhs].trim());
            }
        }

        bfr.close();
        //TODO: convert the map values to corresponding Setup filed using convert ops
        //Iterate over the map
        
        for (Map.Entry<String, String> entry: settingsMap.entrySet()) {
		    setSettingFromKeyValuePair(entry.getKey(),entry.getValue());
		}
        
        //return(map);
	}
	public void writeModelSettingsToFile(File SettingsFile) throws IOException{
		
		//TODO: Add all value key pairs to the parser - MDA 
        TreeMap<String, String> settingsMap = new TreeMap<String, String>();
        BufferedWriter  bfw = new BufferedWriter(new FileWriter(SettingsFile));

        String line;
        
        line = "ageRetire=" + this.ageRetire.toString();
        bfw.write(line);
        bfw.newLine();
        line = "ageSSSelf=" + this.ageSSSelf.toString();
        bfw.write(line);
        bfw.newLine();
        line = "ageSSSpouse=" + this.ageSSSpouse.toString();
        bfw.write(line);
        bfw.newLine();
        line = "ageStart=" + this.ageStart.toString();
        bfw.write(line);
        bfw.newLine();
        line = "incomeDesired=" + this.incomeDesired.toString();
        bfw.write(line);
        bfw.newLine();
        line = "startBalance=" + this.startBalance.toString();
        bfw.write(line);
        bfw.newLine();
        
        //------------
        bfw.close();
        
	}
	
	
}
