package FinanceModelApp;
	
import com.markalexis.financemodel.ModelUtility;
import com.markalexis.financemodel.YearData;
import com.sun.prism.paint.Paint;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;

//TODO: Look at the TabelView control to replace textArea, for a spreadsheet type view
public class Main extends Application {
	
	//Buttons
	Button bthRun = new Button("Run Model");
	//Text
	TextArea textStatus = new TextArea();
	//Grid - TableView
	TableView<YearData> tableV = new TableView<YearData>();
	//Use the Finance Model classes, from the project "FinanceModeler"
	ModelUtility MU = new ModelUtility();
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("FinanceMainForm.fxml"));
			//------------------------------------------------------------------------------------
			// MD Alexis code
			//Grid to arrange buttons and text controls
			GridPane grid = new GridPane();
			grid.setAlignment(Pos.TOP_LEFT);
			grid.setPrefWidth(400);
			//TODO: One column may be better, or a multicolumn single row for buttons.
			ColumnConstraints col1 = new ColumnConstraints();
		     col1.setPercentWidth(90);
		     col1.setMinWidth(200);
		     ColumnConstraints col2 = new ColumnConstraints();
		     col2.setPercentWidth(10);
		     col2.setMinWidth(200);
		     grid.getColumnConstraints().addAll(col1,col2);
		     
			grid.setHgap(10);
			grid.setVgap(10);
			//grid.setGridLinesVisible(true);  //Turn this on for debug and layout
			javafx.geometry.Insets ins = new javafx.geometry.Insets(5,5,5,5);
			grid.setPadding(ins);
			//Fill the grid with controls
			Text scenetitle = new Text("Modeler");
			scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
			grid.add(scenetitle, 0, 0, 2, 1);
			//Add Buttons-----------------------------------------
			bthRun.setOnAction(bthHandlerRun);
			grid.add(bthRun,0,2);
			//grid.add(child, columnIndex, rowIndex);
			
			//Add textArea control to show results
			textStatus.setPrefRowCount(10);
			textStatus.setPrefColumnCount(100);
			textStatus.setWrapText(false);
			textStatus.setPrefWidth(300);
			
			
			grid.add(textStatus, 0, 3);
			
			//-------------------Table View - use to display results --------------------------------------------
			//Set up table columns and their data sources
			TableColumn indexCol = new TableColumn("Index");
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
	        
			tableV.getColumns().addAll(indexCol, yearCol, balanceCol, mktGainCol, incGoalCol, totalIncomeCol);
	        
			grid.add(tableV, 0, 4);
			//------------------------------- Chart ---------------------------------------------
			final CategoryAxis xAxis = new CategoryAxis();
	        final NumberAxis yAxis = new NumberAxis();
	        //final BarChart barchartResults = new BarChart<>(xAxis,yAxis);
	        final AreaChart areachartResults = new AreaChart<>(xAxis,yAxis);
	        
	        areachartResults.setTitle("Results Chart");
	        xAxis.setLabel("Year");       
	        yAxis.setLabel("Dollars");
	        //Series
	        //TODO:  Connect this to an observable list in ModelUtility
	        //final XYChart.Series<int[], double[]> seriesBalance = new XYChart.Series();
	        final XYChart.Series<String, Double> seriesBalance = new Series<String, Double>();
	        seriesBalance.setName("Balance"); 
	        //seriesBalance.setData(MU.balanceDataList);
	        areachartResults.setData(MU.balanceDataList);
	        
	        
	        //Add the Series to the chart
	        areachartResults.getData().addAll(seriesBalance);
	        
			
			grid.add(areachartResults,0,5);
			//------------------------------------------------------------------------------------
			//Below lines are from initial project setup
			//Scene scene = new Scene(root,600,400);

			Scene scene = new Scene(grid,1200,600);
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
	//-------------- Event Handlers ---------------------------------------------------------
		public EventHandler<ActionEvent> bthHandlerRun = new EventHandler<ActionEvent>(){
			//
			//Action When "Run" is pressed,
			//
			@Override
		    public void handle(ActionEvent e) {
		        //String currentText = textStatus.getText();
		        //textStatus.setText(currentText + ", Run button pressed, more text wider than normal to see if the text box will wrap>>>");
				YearData[] yd = MU.runMultiYearModel(30);
				for (int i =0;i<yd.length;i++){
					//Print out a result
					String msg = yd[i].toString();
			        textStatus.appendText( msg +"\n");
			        
			        
					
				}
				tableV.setItems(MU.modelDataList);
		    }
		};
}
