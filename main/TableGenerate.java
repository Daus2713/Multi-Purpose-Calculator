
package main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

//The class create frequency distribution table and summarize the table
public class TableGenerate extends GroupedDataStatistics{
    
    private int numofClass;
    private double[][] classLimit;
    private double[] frequency;
    private HBox layout;
    private GridPane tableGrid;
    private double[] midpoint;
    private double[] freqTimeMid;
    private double[] freqTimeMidPow2;
    private double[] cumltiveFreq;
    private double classWidth;
    private double boundaryFinder;
    private int decimalPlace;
    //Constructor with argument. set value passed to class fields
    public TableGenerate(StackPane tableLayout,int numofClass,double classWidth,int decimalPlace) {
        this.numofClass = numofClass;
        this.classWidth = classWidth;
        this.decimalPlace = decimalPlace;
        createTable(tableLayout); // call to create table
    }
    //This method create frequency distribution table
    private void createTable(StackPane tableLayout) {
        // Clear the existing table
        layout = new HBox();

        tableGrid = new GridPane();
        tableLayout.setTranslateY(15);
        layout.getChildren().add(tableGrid);
        tableGrid.setHgap(0);
        tableGrid.setVgap(0);
        tableGrid.setPadding(new Insets(10));
        tableLayout.getChildren().add(layout);

        // Add column labels
        String[] columnLabels = {"Lower Limit","","Upper Limit", "Frequency", "Midpoint", "f * Xm","f * Xm^2"};
        for (int col = 0; col < columnLabels.length; col++) {
            Label label = new Label(columnLabels[col]);
            label.setFont(Font.font("Segoe Print",11));
            tableGrid.add(label, col, 0);
            

        }
        // Add rows with textfield and label
        for (int row = 1; row <= numofClass; row++) {
            for (int col = 0; col < columnLabels.length; col++) {
                if(col == 1) {
                    Text dash = new Text(" - ");
                    tableGrid.add(dash,col,row);
                }
                else {
                    TextField textField = new TextField();
                    textField.setPrefWidth(70);
                    textField.setStyle("-fx-background-radius: 0;");
                    textField.setEditable(col == 0 || col == 2 || col == 3); 
                    tableGrid.add(textField, col, row);

                    if(col == 5 || col == 6){
                        textField.setPrefWidth(120);
                    }

                }
            }
        }
        //Call to summarize data from table
        calcSummaryData();
}
    //This method get class limit and frquency from table and do error detection 
    private void getClassLimitsAndFrequency() throws Exception{
        
        classLimit = new double[2][numofClass];
        frequency = new double[numofClass];
        
        TextField lowerLimit;
        TextField upperLimit;
        TextField freq;
        //This value use to add and subtract the class limit to get class boundary
        boundaryFinder = (5/(Math.pow(10, decimalPlace+1)));
        
        //Get value from textfield of the table
        for(int i = 0; i< numofClass; i++){
            
            lowerLimit = (TextField)tableGrid.getChildren().get((i * 7) + 7);
            upperLimit = (TextField)tableGrid.getChildren().get((2 + (i * 7))+7);
            freq = (TextField)tableGrid.getChildren().get((3 + (i * 7))+7);
            
            classLimit[0][i] = Double.parseDouble(lowerLimit.getText());//store lower limit
            classLimit[1][i] = Double.parseDouble(upperLimit.getText());//store upper limit
            frequency[i] = Double.parseDouble(freq.getText()); //store frequency
            //check class width
            double checkClassWidth  =(classLimit[1][i] + boundaryFinder) - (classLimit[0][i] - boundaryFinder);
            
            if(classLimit[1][i] < classLimit[0][i]){//Upper limit might less than lower limit
                throw new Exception("Invalid class limit detected!");
            }
            if(i != 0 ){
                if(classLimit[0][i] == classLimit[1][i-1]){
                    throw new Exception("Class limit can't overlap!");
                }
            }
            if(checkClassWidth != classWidth){//find class limit with width that not same with inputted class width
                throw new Exception("class width supposed to be : "+ classWidth);
            }
            if(frequency[i] < 0){
                throw new Exception("Frequency below 0 detected!");
            }          
        }
    }
    //This method override the method in superclass.This method summarize the data in the table    
    @Override
    public void calcSummaryData(){
        //Add button to summarize
        Button summaryData = new Button("Summarize data");
        VBox summaryLayout = new VBox();
        summaryLayout.getChildren().add(summaryData);
        layout.setSpacing(10);
        
        Label errorPrompt = new Label("Invalid input!");
        errorPrompt.setFont(Font.font("Segoe Print",17));
        errorPrompt.setVisible(false);
        layout.getChildren().addAll(summaryLayout,errorPrompt);
        setAnimationButton(summaryData);
        editButtonStyle(summaryData,14);
        //User clicks the button summarize data
        summaryData.setOnAction(event->{
            try {
                summaryLayout.getChildren().retainAll(summaryData);
                getClassLimitsAndFrequency();
                calcFreqTable();
                //do calculation to summarize the data. Also round the number ot 4 decimal place
                double totalFreq = cumltiveFreq[cumltiveFreq.length-1];
                double mean = (BigDecimal.valueOf(mean(freqTimeMid,totalFreq)).setScale(4, RoundingMode.HALF_UP)).doubleValue();
                double[] modeArr = mode(classLimit,frequency,classWidth,boundaryFinder);
                double[]mode=new double[modeArr.length];
                for(int i = 0; i< modeArr.length;i++){
                    mode[i]=(BigDecimal.valueOf(modeArr[i]).setScale(4, RoundingMode.HALF_UP)).doubleValue();
                }
                double median = (BigDecimal.valueOf(median(classLimit,frequency, cumltiveFreq, classWidth, boundaryFinder)).setScale(4, RoundingMode.HALF_UP)).doubleValue();
                double popuVariance = (BigDecimal.valueOf(populationVariance(frequency, midpoint, mean)).setScale(4, RoundingMode.HALF_UP)).doubleValue(); 
                double smpleVariance = (BigDecimal.valueOf(sampleVariance( freqTimeMid,freqTimeMidPow2,totalFreq)).setScale(4, RoundingMode.HALF_UP)).doubleValue();
                //Display the summary of data
                showSummaryData(mean,mode,median,popuVariance,smpleVariance,summaryLayout);
                
                
            }catch(NumberFormatException e){
                errorPrompt.setText("Error :" + e.getMessage());
                errorPrompt.setVisible(true);
            }catch (Exception ex) {
                errorPrompt.setText("Error :" + ex.getMessage());
                errorPrompt.setVisible(true);
            } 
        });
    }
    //This method calculate the value needed to sumamrize the data
    private void calcFreqTable(){
        midpoint = new double[numofClass];
        freqTimeMid = new double[numofClass];
        freqTimeMidPow2 = new double[numofClass];
        cumltiveFreq = new double[numofClass];
        
        for(int i = 0; i < numofClass;i++){
            midpoint[i]= (classLimit[0][i]+classLimit[1][i]) /2;
            freqTimeMid[i] = frequency[i] * midpoint[i];
            freqTimeMidPow2[i] = frequency[i] * (Math.pow(midpoint[i], 2));
            if(i == 0){
                cumltiveFreq[i]=frequency[i];
            }else{
                cumltiveFreq[i] = frequency[i] + cumltiveFreq[i-1];
            }
        }
        
        TextField midpnt;
        TextField ftm;
        TextField ftmp2;
        //Add the calculated value to table
        for(int i = 0;i < numofClass;i++){
            midpnt = (TextField)tableGrid.getChildren().get(4+(i * 7) + 7);
            ftm = (TextField)tableGrid.getChildren().get((5 + (i * 7))+7);
            ftmp2 = (TextField)tableGrid.getChildren().get((6 + (i * 7))+7);
            
            
            midpnt.setText(Double.toString(midpoint[i]));
            ftm.setText(Double.toString(freqTimeMid[i]));
            ftmp2.setText(Double.toString(freqTimeMidPow2[i]));
            
        }
    }
    //The method show the summary of data using the value calculated before
    private void showSummaryData(double mean,double[]mode,double median, double popuVariance,double smpleVariance,VBox summaryLayout){
        
        Text txt = new Text("Number of Classes : " + numofClass + " , Class width : " + classWidth + ", Data decimal place : " + decimalPlace);
        Text meantxt = new Text ("Mean: " + mean);
        Text modetxt = new Text ("Number of mode founded: " + mode.length);

        Text[] modeArr = new Text[mode.length];
        for(int i = 0; i < mode.length; i++){
            modeArr[i] = new Text("Mode #" + (i+1) + " : " +  mode[i]);
            editTextStyle(modeArr[i],"Segoe Print",14);
        }
        Text mediantxt = new Text ("Median : " + median);
        Text popuVartxt = new Text("Population Variance : " + popuVariance);
        Text smplVartxt = new Text("Sample Variance : " + smpleVariance);
        
        summaryLayout.setSpacing(10);
        summaryLayout.getChildren().addAll(txt,meantxt,modetxt);
        //Save the calcuation to history
        ArrayList <String> save = new ArrayList<>();
        save.add("Grouped Data Statistics - Table summary");
        for(int i = 0; i< numofClass; i++){
            save.add(classLimit[0][i]+" - "+classLimit[1][i] + "   Frequency: "+ frequency[i]);
        }
        //Record history
        save.add(meantxt.getText());
        save.add(modetxt.getText());
        
        for(int i = 0;i< mode.length;i++){
            summaryLayout.getChildren().add(modeArr[i]);
            save.add(modeArr[i].getText());
        }
        
        save.add(mediantxt.getText());
        save.add(popuVartxt.getText());
        save.add(smplVartxt.getText());
        
        editTextStyle(txt,"Segoe Print",14);
        editTextStyle(meantxt,"Segoe Print",14);
        editTextStyle(modetxt,"Segoe Print",14);
        editTextStyle(mediantxt,"Segoe Print",14);
        editTextStyle(popuVartxt,"Segoe Print",14);
        editTextStyle(smplVartxt,"Segoe Print",14);
        
        //Call to save into the file
        historyRecord(save);
        
        summaryLayout.getChildren().addAll(mediantxt,popuVartxt,smplVartxt);
    }
    //This method override method in superclass to record history to file
    @Override
    public void historyRecord(ArrayList<String>input){
        String[] arr = input.toArray(String[]::new);
        history.addWord(arr);
  
    } 

}
    
    
    
    
    

