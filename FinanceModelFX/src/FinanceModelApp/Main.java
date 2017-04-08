package FinanceModelApp;
	
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.prefs.Preferences;

import com.markalexis.financemodel.ModelUtility;
import com.markalexis.financemodel.YearData;
import com.sun.prism.paint.Paint;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;

//TODO: Look at the TabelView control to replace textArea, for a spreadsheet type view
public class Main extends Application {
	
	//Menu
	MenuBar menuBar = new MenuBar();
    Menu menuFile = new Menu("File");
    MenuItem menuFileSaveSettings = new MenuItem("Save Settings to File");
    MenuItem menuFileOpenSettings = new MenuItem("Open Settings File");
    Menu menuEdit = new Menu("Edit");

    Menu menuView = new Menu("View");

    //menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
	
	//Buttons
	Button btnRun = new Button("Run Model");
	Button btnTest = new Button("Test");
	//Text
	final TextArea textStatus = new TextArea();
	//Grid - TableViews for results and setup parameters
	final TableView<YearData> tableResults = new TableView<YearData>();
	TableView<Map> tableModelSetup = null;
	
	AreaChart areachartResults = null;
	AreaChart areachartIncome = null;
	
	//The form has one tabbed pane to which we add content
	TabPane tabPaneMain = new TabPane();
		
	//Each tab will have a grid to control layout
	final GridPane gridMain = new GridPane();
	final GridPane gridSetup = new GridPane();
	final GridPane gridModelSettings = new GridPane();
	final GridPane gridButtons = new GridPane();
	
	
	
	//Text boxes for Setup tab
	TextField txtStartBalance = new TextField("0");
	TextField txtStartYear = new TextField("");
	TextField txtAgeStart= new TextField("0");
	TextField txtAgeRetire = new TextField("0");
	TextField txtAgeSSSelf = new TextField("0");
	TextField txtAgeSSSpouse = new TextField("0");
	TextField txtPensionIncome = new TextField("0");
	TextField txtSocialSecurityIncomeSelf = new TextField("0");
	TextField txtSocialSecurityIncomeSpouse = new TextField("0");
	TextField txtIncomeDesired = new TextField("0");
	TextField txtSavingsContributions= new TextField("0");
	
	//Use the Finance Model classes, from the project "FinanceModeler"
	ModelUtility MU = new ModelUtility();
	public Stage  primaryStage;
	//public void start(Stage primaryStage) {
	
	@Override
	public void start( Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("FinanceMainForm.fxml"));
			//------------------------------------------------------------------------------------
			// MD Alexis code 	
			//------------------------------------------------------------------------------------
			
			//Set up handlers for controls
			btnRun.setOnAction(btnHandlerRun);
			btnTest.setOnAction(btnHandlerTest);
			
			//Build each control
			
			
			//Build others in methods for modularity
			buildMenuBar();
			buildChartResults();
			buildChartIncome();
			buildTextStatus();
			buildTableResults();
			buildTabPane();
			buildGridButtons();
			buildGridMain();
			buildGridSetup();
			buildGridModelSettings();
			buildTableModelSetup();
			
			gridModelSettings.add(tableModelSetup, 0, 1);	
			
			Text scenetitle = new Text("Modeler");
			scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
			//gridMain.add(scenetitle, 0, 0, 2, 1);
			
			
			//Add menu, then Buttons, and the tabs below buttons-----------------------------------------
			gridMain.add(menuBar, 0, 0,2,1);
			gridMain.add(gridButtons,0,1,2,1);
			gridMain.add(tabPaneMain, 0, 2,2,1);
			
			
			Scene scene = new Scene(gridMain,1200,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	//----------------Use App Preferences to store and retrieve the Model parameters
	public void getModelPreferences(){
		try{
		//Read the current settings from preferences, a persistent store
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String prefAsString = "";
		//First parse, values are all strings and must be converted
		prefAsString = prefs.get("startBalance", null);
		MU.MS.startBalance = Double.parseDouble(prefAsString);
		//...etc for all parameters
		} catch(Exception e) {
			e.printStackTrace();
		}  	
	}
	public void setModelPreferences(){
		//Write the current settings to preferences, a persistent store
		try{
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		
		//...etc for all parameters
		prefs.put("startYear", MU.MS.startYear.toString());
		prefs.put("percentInvestedInMarketPost", MU.MS.percentInvestedInMarketPost.toString());
		
		prefs.put("percentinvestedInMarketPre", MU.MS.percentinvestedInMarketPre.toString());
		prefs.put("ageStart", MU.MS.ageStart.toString());
		prefs.put("ageRetire", MU.MS.ageRetire.toString());
		prefs.put("ageSSSelf", MU.MS.ageSSSelf.toString());
		prefs.put("ageSSSpouse", MU.MS.ageSSSpouse.toString());
		
		prefs.put("startBalance", MU.MS.startBalance.toString());
		prefs.put("pensionIncome", MU.MS.pensionIncome.toString());
		
		prefs.put("socialSecurityIncomeSelf", MU.MS.socialSecurityIncomeSelf.toString());
		prefs.put("socialSecurityIncomeSpouse", MU.MS.socialSecurityIncomeSpouse.toString());
		prefs.put("incomeDesired", MU.MS.incomeDesired.toString());
		
		prefs.put("inflationRateStatic", MU.MS.inflationRateStatic.toString());
		prefs.put("savingsContributions", MU.MS.savingsContributions.toString());
		prefs.put("maxPercentWithdraw", MU.MS.maxPercentWithdraw.toString());
		prefs.put("marketReturnsStatic", MU.MS.marketReturnsStatic.toString());
		prefs.put("stableReturnsStatic", MU.MS.stableReturnsStatic.toString());
		
		
		
		} catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	//--------------- Build up each control
	public void refreshSetupValues(){
		//Used at startup, fills in the textField boxes with the underlying data
		txtStartBalance.setText(MU.MS.startBalance.toString()); 
		txtStartYear.setText(MU.MS.startYear.toString());
		txtAgeStart.setText(MU.MS.ageStart.toString());
		txtAgeRetire.setText(MU.MS.ageRetire.toString());
		txtAgeSSSelf.setText(MU.MS.ageSSSelf.toString());
		txtAgeSSSpouse.setText(MU.MS.ageSSSpouse.toString());
		txtPensionIncome.setText(MU.MS.pensionIncome.toString());
		txtSocialSecurityIncomeSelf.setText(MU.MS.socialSecurityIncomeSelf.toString());
		txtSocialSecurityIncomeSpouse.setText(MU.MS.socialSecurityIncomeSpouse.toString());
		txtIncomeDesired.setText(MU.MS.incomeDesired.toString());
		txtSavingsContributions.setText(MU.MS.savingsContributions.toString());
		
	}
	public void buildGridSetup(){
		//------------------- ----------------------------------------
		//Grid to arrange controls on the Setup Tab grid arrange controls
		//
		//
		//---------------------------------------------------------------------------------------------------
		
		gridSetup.setAlignment(Pos.TOP_LEFT);
		gridSetup.setPrefWidth(100);
		//Set up two columns to arrange the controls
		//TODO: Adjust the widths for appearance
		ColumnConstraints col1 = new ColumnConstraints();
	     //col1.setPercentWidth(50);
	     col1.setMinWidth(50);
	     ColumnConstraints col2 = new ColumnConstraints();
	     //col2.setPercentWidth(50);
	     col2.setMinWidth(50);
	     gridSetup.getColumnConstraints().addAll(col1,col2);
	     
		gridSetup.setHgap(1);
		gridSetup.setVgap(1);
		
		//Data entry fields and labels, text fields are declared as globals at the top
		int i = 0;
		gridSetup.add(new Label("Savings Parameters"), 0,i); i++;
		gridSetup.add(new Label("Current Savings"), 0,i); gridSetup.add(txtStartBalance, 1, i);i++;
		gridSetup.add(new Label("Annual savings (401k,etc)"), 0,i); gridSetup.add(txtSavingsContributions, 1,i); i++;
		gridSetup.add(new Label("Time Parameters"), 0,i); i++;
		gridSetup.add(new Label("Model Start Year"), 0,i); gridSetup.add(txtStartYear, 1, i);i++;
		gridSetup.add(new Label("Your Age"), 0,i); gridSetup.add(txtAgeStart, 1, i);i++;
		gridSetup.add(new Label("Retirement Age"), 0,i); gridSetup.add(txtAgeRetire, 1, i);i++;
		gridSetup.add(new Label("Start SS at age"), 0,i);gridSetup.add(txtAgeSSSelf, 1, i);i++;
		gridSetup.add(new Label("Start SS age - spouse"), 0,i);gridSetup.add(txtAgeSSSpouse, 1, i);i++;
		//gridSetup.add(new Label(""), 0,i); i++;
		gridSetup.add(new Label("Income Parameters"), 0,i); i++;
		gridSetup.add(new Label("Desired Income - in today dollars"), 0,i);
		gridSetup.add(txtIncomeDesired, 1, i);i++;
		gridSetup.add(new Label("SS Income - self"), 0,i); 
		gridSetup.add(txtSocialSecurityIncomeSelf, 1, i);i++;
		gridSetup.add(new Label("SS Income - Spouse"), 0,i);
		gridSetup.add(txtSocialSecurityIncomeSpouse, 1, i);i++;
		gridSetup.add(new Label(""), 0,i); 
		gridSetup.add(txtPensionIncome, 1, i);i++;
		gridSetup.add(new Label(""), 0,i); i++;
		gridSetup.add(new Label("Investment Parameters"), 0,i); i++;
		gridSetup.add(new Label(""), 0,i); i++;
		gridSetup.add(new Label(""), 0,i); i++;
			
	}
	public void buildGridButtons(){
		//Use a grid to contain buttons on the Main Grid
		gridButtons.setAlignment(Pos.TOP_LEFT);
		//Add the buttons
		gridButtons.add(btnRun,0,0);
		gridButtons.add(btnTest,1,0);
		
	}
	public void buildMenuBar(){
		//The menu bar
		menuFile.getItems().addAll(menuFileSaveSettings, menuFileOpenSettings);
		menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
		//set up handler
		menuFileSaveSettings.setOnAction(menuFileSaveSettingsHandler);  
		menuFileOpenSettings.setOnAction(menuFileOpenSettingsHandler);  
		//btnTest.setOnAction(btnHandlerTest);
		
	}
	public void buildTabPane(){
		//TODO: Make a TabPane the main parent control so we can add a tab for model setup parameters
		
		//First Tab and content
	 	
	 	
	 	Tab tabResults = new Tab();
	 	tabResults.setText("Results");
	 	tabResults.setContent(tableResults);
	 	//gridMain.add(textStatus, 0, 3, 2, 1);
	 	//gridMain.add(tableResults, 0, 4,2,1);
	 	tabPaneMain.getTabs().add(tabResults);
	 	
	 	//Create tab for a chart with Income info
	 	Tab tabIncome = new Tab();
	 	tabIncome.setText("Income");
	 	tabIncome.setContent(areachartIncome);
	 	tabPaneMain.getTabs().add(tabIncome);
	 	
	 	//Create tab for a chart with asset info
	 	Tab tabAssets = new Tab();
	 	tabAssets.setText("Assets");
	 	tabAssets.setContent(areachartResults);
	 	tabPaneMain.getTabs().add(tabAssets);
	 	
	 	//Create Third tab for a tableview with setup parameters
	 	Tab tabModelSettings = new Tab();
	 	tabModelSettings.setText("Model Settings");
	 	tabModelSettings.setContent(gridModelSettings);
	 	tabPaneMain.getTabs().add(tabModelSettings);
	 	
	 	Tab tabSetup = new Tab();
	 	tabSetup.setText("Setup");
	 	tabSetup.setContent(gridSetup);
	 	tabPaneMain.getTabs().add(tabSetup);
	 	
	 	Tab tabStatus = new Tab();
	 	tabStatus.setText("Status");
	 	tabStatus.setContent(textStatus);
	 	tabPaneMain.getTabs().add(tabStatus);
	 	
	 	
	 	refreshSetupValues();
	 	
	 	
	}
	public void buildGridMain(){
		//-------------------Main grid arrange controls on Main Tab ----------------------------------------
		//
		//Grid to arrange buttons and text controls
		//The grid is the parent of all the controls on the form
		// One column only
		//---------------------------------------------------------------------------------------------------
		gridMain.setAlignment(Pos.TOP_LEFT);
		gridMain.setPrefWidth(400);//was 400
		//TODO: One column may be better, or a multicolumn single row for buttons.
		ColumnConstraints col1 = new ColumnConstraints();
	    col1.setPercentWidth(100);//This setting is critical, it allows grid to fill the parent
	    col1.setMinWidth(5);//was 200
	    //ColumnConstraints col2 = new ColumnConstraints();
	    //col2.setPercentWidth(10);
	    //col2.setMinWidth(5);
	    gridMain.getColumnConstraints().addAll(col1);
	     
		gridMain.setHgap(1);
		gridMain.setVgap(1);
		gridMain.setGridLinesVisible(true);  //Turn this on for debug and layout
		javafx.geometry.Insets ins = new javafx.geometry.Insets(5,5,5,5);
		gridMain.setPadding(ins);		
	}
	public void buildGridModelSettings(){
		//------------------- Grid to arrange controls on Model settings Tab ----------------------------------------
		//
		//Grid to arrange buttons and text controls
		//The grid is the parent of all the controls on the tab
		//---------------------------------------------------------------------------------------------------
		
		gridModelSettings.setAlignment(Pos.TOP_LEFT);
		gridModelSettings.setPrefWidth(400);
		//TODO: One column may be better, or a multicolumn single row for buttons.
		ColumnConstraints col1 = new ColumnConstraints();
	     col1.setPercentWidth(90);
	     col1.setMinWidth(200);
	     ColumnConstraints col2 = new ColumnConstraints();
	     col2.setPercentWidth(10);
	     col2.setMinWidth(200);
	     gridModelSettings.getColumnConstraints().addAll(col1,col2);
	     
		gridModelSettings.setHgap(10);
		gridModelSettings.setVgap(10);
		gridModelSettings.setGridLinesVisible(true);  //Turn this on for debug and layout
		javafx.geometry.Insets ins = new javafx.geometry.Insets(5,5,5,5);
		gridModelSettings.setPadding(ins);		
	}
	public void buildTextStatus(){
		//-------------------Text Status - use to display messages ------------------------------------------
		//
		//
		//---------------------------------------------------------------------------------------------------
		//Add textArea control to show results
		textStatus.setPrefRowCount(10);
		textStatus.setPrefColumnCount(100);
		textStatus.setWrapText(false);
		textStatus.setPrefWidth(300);		
	}
	
	public void buildChartResults(){
		//-------------------Results Chart - use to display results ----------------------------------------
		//
		//---------------------------------------------------------------------------------------------------
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        //final BarChart barchartResults = new BarChart<>(xAxis,yAxis);
        areachartResults = new AreaChart<>(xAxis,yAxis);
        
        areachartResults.setTitle("Results Chart");
        xAxis.setLabel("Age");       
        yAxis.setLabel("Dollars");
        //Series
        //TODO:  Connect this to an observable list in ModelUtility
        //final XYChart.Series<int[], double[]> seriesBalance = new XYChart.Series();
        final XYChart.Series<String, Double> seriesBalance = new Series<String, Double>();
        seriesBalance.setName("Balance"); 
        //seriesBalance.setData(MU.balanceDataList);
        //areachartResults.setData(MU.balanceDataList);
        
        
        //Add the Series to the chart
        areachartResults.getData().addAll(MU.balanceSeries);
        
		
		
		
	}
	public void buildChartIncome(){
		//-------------------Income Chart - use to display income vs time ----------------------------------------
		//
		//---------------------------------------------------------------------------------------------------
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        //final BarChart barchartResults = new BarChart<>(xAxis,yAxis);
        areachartIncome = new AreaChart<>(xAxis,yAxis);
        areachartIncome.setTitle("Income Chart");
        xAxis.setLabel("Age");       
        yAxis.setLabel("Dollars");
        //Use series data in Model Utility to display on this chart
        //The underlying series data is an obeservable list, so it updates with each model run
        areachartIncome.getData().addAll(MU.incomeSeries, MU.incomeGoalSeries, MU.savingsDrawSeries);
	}
	public void buildTableResults()
	{
		//-------------------Table View - use to display results --------------------------------------------
		//
		//---------------------------------------------------------------------------------------------------
		//Set up table columns and their data sources
		TableColumn indexCol = new TableColumn("Index");
		TableColumn ageCol = new TableColumn("Age");
		ageCol.setCellValueFactory(new PropertyValueFactory<YearData,String>("age"));	
        TableColumn yearCol = new TableColumn("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<YearData,String>("year"));	
        TableColumn balanceCol = new TableColumn("StartBalance");
		balanceCol.setCellValueFactory(new PropertyValueFactory<YearData,String>("startBalanceFormatted"));
        TableColumn mktGainCol = new TableColumn("Gains");
        mktGainCol.setCellValueFactory(new PropertyValueFactory<YearData, String>("marketGainsFormatted"));
        TableColumn incGoalCol = new TableColumn("Income Goal");
        incGoalCol.setCellValueFactory(new PropertyValueFactory<YearData, String>("incomeGoalWithInflationFormatted"));
        TableColumn totalIncomeCol = new TableColumn("Total Income");
        totalIncomeCol.setCellValueFactory(new PropertyValueFactory<YearData, String>("incomeTotalFormatted"));
        
		tableResults.getColumns().addAll(indexCol, ageCol, yearCol, balanceCol, mktGainCol, incGoalCol, totalIncomeCol);
	}
	@SuppressWarnings("unchecked")
	public void buildTableModelSetup()
	{
		//-------------------Table View - use to display results --------------------------------------------
		//
		//---------------------------------------------------------------------------------------------------
		//Set up table columns and their data sources
		TableColumn<Map, String> parameternameCol = new TableColumn("Parameter");
		//parameternameCol.setCellValueFactory((Callback<CellDataFeatures<Map, String>, ObservableValue<String>>) new MapValueFactory(Column1MapKey));
		//parameternameCol.setCellValueFactory(new MapValueFactory<String>();
		
		TableColumn<Map, String> parametervalueCol = new TableColumn("Value");
		tableModelSetup = new TableView<Map>();
		
		tableModelSetup.setEditable(true);
		//tableModelSetup.setItems( MU.MS.obsModelMap);
		//tableResults.setItems(MU.modelDataList);
		tableModelSetup.getColumns().addAll(parameternameCol, parametervalueCol);
	}
	public void showSettingsMap(){
	//Displays the settings Map key/value pairs
		String msg="";
		//Map map = MU.MS.settingsMap;
		
		
		//for (Map.Entry<String, Object> entry : map.entrySet()) {
	    //System.out.println("key=" + entry.getKey() + ", value=" + entry.getValue());
		
		// From the java tutorials: for (Map.Entry<KeyType, ValType> e : m.entrySet())
		
		for (Map.Entry<String, String> entry: MU.MS.settingsMap.entrySet()) {
		    msg ="key=" + entry.getKey() + ", value=" + entry.getValue();
		    textStatus.appendText( msg +"\n");
		}
		
	}
	public File chooseSettingsFile(){
		//Note returned file may be null
		FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Open Model Settings File");
		 fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
		 File selectedFile = fileChooser.showOpenDialog(primaryStage); //primaryStage
		 //if (selectedFile != null){ };
		 return selectedFile;
	}
	public File chooseSaveSettingsFile(){
		//Note differences between save and open file dialogues
		// Can return null
		FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Save Model Settings File");
		 fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
		 File selectedFile = fileChooser.showSaveDialog(primaryStage); //primaryStage
		 //if (selectedFile != null){ };
		 return selectedFile;
	}
	private void saveSettings(){
		//Save current settings to file

		File sFile = chooseSaveSettingsFile();
        if(sFile!=null) {
        
        try {
			MU.MS.writeModelSettingsToFile(sFile);
			} catch (IOException e1) {
				handleErrors(e1.getMessage());
			}
        
        showSettingsMap();
        //TODO: make sure these settings are written to ModelSetup variables
        
        }
	}
	private void openSettingsFile(){
		//Open and parse a settings file
		//sFile = new File("./settings.txt");
		File sFile = chooseSettingsFile();
        if(sFile!=null) {
        
        try {
			MU.MS.readModelSettingsFromFile(sFile);
			} catch (IOException e1) {
				handleErrors(e1.getMessage());
			}
        
        showSettingsMap();
        //TODO: make sure these settings are written to ModelSetup variables
        }
	}
	private void handleErrors(String errorMessage){
		//Display errors in status window
        textStatus.appendText( "ERRROR: " + errorMessage +"\n");
	}
	//-------------- Event Handlers ---------------------------------------------------------
	
	
	public EventHandler<ActionEvent> menuFileSaveSettingsHandler = new EventHandler<ActionEvent>(){
		//
		//Action When 
		//
		@Override
	    public void handle(ActionEvent e) {
			saveSettings();
			}
		};
	public EventHandler<ActionEvent> menuFileOpenSettingsHandler = new EventHandler<ActionEvent>(){
		//
		//Action When 
		//
		@Override
	    public void handle(ActionEvent e) {
			openSettingsFile();
			}
		};
	public EventHandler<ActionEvent> btnHandlerTest = new EventHandler<ActionEvent>(){
		//
		//Action When "Test" is pressed,
		//
		@Override
	    public void handle(ActionEvent e) {
			
			String msgTest = "Testing Preferences, and settings file....";
	        textStatus.appendText( msgTest +"\n");
	        setModelPreferences();
	        getModelPreferences();
	        openSettingsFile();
	        msgTest = "Testing completed.";
	        textStatus.appendText( msgTest +"\n");
			}
		};
		
	public EventHandler<ActionEvent> btnHandlerRun = new EventHandler<ActionEvent>(){
			//
			//Action When "Run" is pressed,
			//
			@Override
		    public void handle(ActionEvent e) {
		        //String currentText = textStatus.getText();
		        //textStatus.setText(currentText + ", Run button pressed, more text wider than normal to see if the text box will wrap>>>");
				
				int countSuccess =0;
				int countTotalYears =0;
				YearData[] yd = MU.runMultiYearModel(40);
				for (int i =0;i<yd.length;i++){
					//Print out a result
					String msg = yd[i].toString();
			        textStatus.appendText( msg +"\n");
			        //Counter updates
			        if(yd[i].retired){
			        	countTotalYears++;
			        	if (yd[i].incomeGoalMet) countSuccess++;
			        }
					
				}
				tableResults.setItems(MU.modelDataList);
				
				//Show the Succcess rate
				
				String msgSuccess = "Income goal met in "+ countSuccess + " of " + countTotalYears + " years.";
		        textStatus.appendText( msgSuccess +"\n");
		    }
		};
}
