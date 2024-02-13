package main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


//The class calculate grouped data statistics. perform two ways to calculate reuslt: 1. method 2. table
public class GroupedDataStatistics extends IndividualDataStatistics{
    //Create contructor with argument
    public GroupedDataStatistics(StackPane root){
        createGroupStatsScene(root);
    }
    //Create default contructor
    public GroupedDataStatistics(){
        
    }

    //This method find mode from table data. Mode can be more than two
    protected double[] mode(double[][]classLimit,double[]frequency,double classWidth,double boundaryFinder){

        ArrayList <Integer> indexClass = new ArrayList<>();
        double maxFreq =0;
        
        //find highest frequency
        for(int i =0; i <frequency.length;i++){
            if(frequency[i] > maxFreq){
                maxFreq = frequency[i];
            }
        }
        //find index class that has highest frequecny. Could be more than one class
        for(int i =0; i <frequency.length;i++){
            if(frequency[i] == maxFreq){
                indexClass.add(i);
            }
        }
        
        double [] ans = new double[indexClass.size()];
        //calculate grouped data mode fore each class mode
        for(int i = 0;i<indexClass.size();i++){
            
            int index = indexClass.get(i);
            double fm = frequency[index];
            double fmMinus1;
            if(indexClass.get(i) == 0) // the index before the index class mode is below 0
                fmMinus1 = 0;
            else
                fmMinus1 = frequency[index - 1];
            
            
            double fmPlus1;
            if(indexClass.get(i) == (classLimit[0].length-1))// the index before the index class mode is the last index
                fmPlus1 = 0;
            else
                fmPlus1 = frequency[index + 1];
            
            
            double lowerBoundary = classLimit[0][index] - boundaryFinder;
            double value = (fm - fmMinus1)/((fm - fmMinus1 ) + (fm - fmPlus1));
            ans[i] = lowerBoundary + value * classWidth;
        }
        
        return  ans;
    }
    //This method calculate mean and return result in double
    protected double mean(double[]freqTimeMid,double totalFreq){
        double sumFreqTimeMid =0;
        for(int i = 0; i< freqTimeMid.length;i++){
            sumFreqTimeMid += freqTimeMid[i];
        }
        
        return sumFreqTimeMid / totalFreq;
    }
    //This method calculate median and return result in double
    protected double median(double[][]classLimit,double[]frequency,double[] cumltiveFreq,double classWidth,double boundaryFinder){
        
        double cumltiveFrqMedian = cumltiveFreq[cumltiveFreq.length-1] / 2;
        int medianIndex = 0;
        for (int i = 0; i < cumltiveFreq.length; i++) {
            
            if (cumltiveFreq[i] >= cumltiveFrqMedian) {
                medianIndex = i;
                break;
            }
        }
        double freqMedian = frequency[medianIndex];
        double lowerBoundary = classLimit[0][medianIndex] - boundaryFinder;
        double cmltiveBeforeMedianClass;
        if(medianIndex == 0){
            cmltiveBeforeMedianClass = 0;
        }else{
            cmltiveBeforeMedianClass = cumltiveFreq[medianIndex - 1];
        }
        
        double median = lowerBoundary + ((cumltiveFrqMedian - cmltiveBeforeMedianClass)/freqMedian)*classWidth;
        
        return median;
    }
    
    //This method calculate population variance and return result in double
    protected double populationVariance(double[] frequency, double[] midpoint, double mean) {
        double sumSquaredDeviations = 0.0;
        double totalFrequency = 0.0;

        // Calculate the sum of squared deviations
        for (int i = 0; i < frequency.length; i++) {
            double deviation = midpoint[i] - mean;
            double squaredDeviation = Math.pow(deviation, 2);
            double groupSumSquaredDeviations = squaredDeviation * frequency[i];
            sumSquaredDeviations += groupSumSquaredDeviations;
            // Update the total frequency
            totalFrequency += frequency[i];
        }
        // Calculate the population variance
        double populationVariance = sumSquaredDeviations / totalFrequency;

        return populationVariance;
    }
    
    //This method calculate sample variance and return result in double
    protected double sampleVariance(double[] freqTimeMid,double[]freqTimeMidPow2,double totalFreq){
        
        
        double sumFreqTimeMid=0;
        double sumFreqTimeMidPow2=0;
        for(int i = 0; i< freqTimeMid.length;i++){
            sumFreqTimeMid += freqTimeMid[i];
            sumFreqTimeMidPow2 += freqTimeMidPow2[i];
        }
        
        double numerator = totalFreq * sumFreqTimeMidPow2 - Math.pow(sumFreqTimeMid, 2);
        double denominator = totalFreq * (totalFreq - 1);
        
        return numerator / denominator;
    }

    //This method create the layout for grouped data page scene
    private void createGroupStatsScene(StackPane root){
        //Creaet radio button. User can choose to calculate using method or table
        RadioButton formula = new RadioButton("Methods");
        RadioButton table = new RadioButton("Table");
        Text instruction = new Text("Which option would you like to choose:");
        Button reset = new Button("Reset");
        ToggleGroup group = new ToggleGroup();
        formula.setToggleGroup(group);
        table.setToggleGroup(group);
        
        HBox radioBtnGroup = new HBox(instruction,formula,table,reset);
        radioBtnGroup.setSpacing(10);
        //Create sublayout and main layout
        VBox layout = new VBox();
        VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(radioBtnGroup,layout);
        StackPane.setAlignment(mainLayout, Pos.TOP_LEFT);
        
        radioBtnGroup.setTranslateX(30);
        radioBtnGroup.setTranslateY(60);
        layout.setTranslateX(30);
        layout.setTranslateY(70);
        editButtonStyle(reset,14);
        editTextStyle(instruction,"Segoe Print",12);
        editTextStyle(instruction,"Segoe Print",12);
        formula.setFont(Font.font("Segoe Print",12));
        table.setFont(Font.font("Segoe Print",12));
        //Add main layout in root node
        root.getChildren().addAll(mainLayout);
 
        //Add layout to add other nodes
        HBox hbox = new HBox();
        TextField classNum = new TextField();
        TextField classWidth = new TextField();
        classWidth.setPromptText("Insert class width");
        classNum.setPromptText("Insert number of class");
        Label errorPrompt = new Label("");
        
        errorPrompt.setVisible(false);
        errorPrompt.setFont(Font.font("Segoe Print",14));
        numDecimal.setPromptText("Decimal place(default 0)"); //set decimal place to 0
        Button generateTable = new Button("Generate Frequency Table");
        Button resetBtn = new Button("Reset");
        resetBtn.setDisable(true);
        
        hbox.getChildren().addAll(classNum,classWidth,numDecimal,generateTable,resetBtn,errorPrompt);
        hbox.setSpacing(27);
        
        setAnimationButton(generateTable);
        editButtonStyle(generateTable,14);
        setAnimationButton(resetBtn);
        editButtonStyle(resetBtn,14);
        
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == formula) {//If user choose method/formula

                createMethodLayout(layout);
                formula.setDisable(true);
                table.setDisable(true);
            } else if(newValue == table){//if user choose table
                    layout.getChildren().add(hbox);
                    formula.setDisable(true);
                    table.setDisable(true); 
            }
        });
        //Click reset button to choose again
        reset.setOnAction(e->{
            layout.getChildren().clear();
            numDecimal.setText("");
            classWidth.setText("");
            classNum.setText("");
            formula.setDisable(false);
            table.setDisable(false);
            formula.setSelected(false);
            table.setSelected(false);
        }); 
        
        //After user has chose table option and has inputted required value and proceed to generate table
        generateTable.setOnAction(event->{
            try{
                StackPane tableLayout = new StackPane();
                int numberofClass = Integer.parseInt(classNum.getText());//Get number of class value
                int decimalPlace = 0;//Get decimal place value
                errorPrompt.setVisible(false);
                
                if(!(numDecimal.getText().isEmpty()))//If user put custom decimal place
                    decimalPlace = Integer.parseInt(numDecimal.getText());

                double width = Double.parseDouble(classWidth.getText());//Get class width
                if(numberofClass > 20){//This program only handle class not greater than 20
                    throw new Exception("Can't exceed 20 classes");
                }

                layout.getChildren().add(tableLayout);
                //Create instance of TableGenerate to create table
                TableGenerate groupTable = new TableGenerate(tableLayout,numberofClass,width,decimalPlace);
                resetBtn.setDisable(false);
                generateTable.setDisable(true);
            //Click reset button to re-input new value    
            resetBtn.setOnAction(eve->{
                layout.getChildren().remove(tableLayout);
                generateTable.setDisable(false);
            });
                
            }catch(NumberFormatException e){
                errorPrompt.setText("Invalid input!");
                errorPrompt.setVisible(true);

            }catch(Exception e){
                errorPrompt.setText(e.getMessage());
                errorPrompt.setVisible(true);
            }finally{
                //Add feature that make value clear when user click the text field again
                classNum.setOnMouseClicked(event2 -> classNum.clear());
                classWidth.setOnMouseClicked(event2 -> classWidth.clear());
                numDecimal.setOnMouseClicked(event2 -> numDecimal.clear());
            }
        });
    }
    
    ////Create all methods in Grouped data category
    private void createMethodLayout(VBox layout){
        

        createMethodGroup("Mean,\u00B5(Sample/Population)",2,new String[]{"\u2211 f * X","N"},0,0);
        createMethodGroup("Mode(Grouped Data)",5,new String[]{"L","fm","fm-1","fm+1","w"},1,0);
        createMethodGroup("Median(Grouped Data)",5,new String[]{"L","n","B","G","W"},2,0);
        createMethodGroup("Variance(Population)",2,new String[]{"\u2211 (X - \u00B5)^2","n"},3,0);
        createMethodGroup("Variance(Sample)",3,new String[]{"\u2211 f * X^2", "(\u2211 f * X)^2","n"},0,1);
        methodLayout.setStyle(" -fx-background-radius: 30;-fx-font-size: 13px;");
               
        layout.getChildren().addAll(methodLayout);

        methodLayout.setHgap(15);
        methodLayout.setVgap(15);
        
        
    }   
    //This method override the method in individual data class. perform same function but for grouped data
    @Override
    public void performCalculation(String methodName,TextField []textInput,TextField resultText){
            double value = 0.0;
            String errorPrompt = "Invalid input!";
            ArrayList<String> input = new ArrayList<>();//store the input to record history
            try{
                
                if("Mean,\u00B5(Sample/Population)".equals(methodName)){
                    double fX = Double.parseDouble(textInput[0].getText());
                    double n = Double.parseDouble(textInput[1].getText());
                    value = fX/n;
                    
                    input.add(Double.toString(fX));
                    input.add(Double.toString(n));
                }
                else if("Mode(Grouped Data)".equals(methodName)){
                    double lower = Double.parseDouble(textInput[0].getText());
                    double fBefore = Double.parseDouble(textInput[2].getText());
                    double fm = Double.parseDouble(textInput[1].getText());
                    double fAfter = Double.parseDouble(textInput[3].getText());
                    double width = Double.parseDouble(textInput[4].getText());
                    value = lower +((fm - fBefore) / ((fm-fBefore)+(fm-fAfter)))*width;
                    
                    input.add(Double.toString(lower));
                    input.add(Double.toString(fm)); 
                    input.add(Double.toString(fBefore)); 
                    input.add(Double.toString(fAfter)); 
                    input.add(Double.toString(width)); 
                    
                }
                else if("Median(Grouped Data)".equals(methodName)){
                    double lower = Double.parseDouble(textInput[0].getText());
                    double n = Double.parseDouble(textInput[1].getText());
                    double B = Double.parseDouble(textInput[2].getText());
                    double G = Double.parseDouble(textInput[3].getText());
                    double width = Double.parseDouble(textInput[4].getText());
                    value = lower + (((n/2)-B)/G)* width;
                    
                    input.add(Double.toString(lower));
                    input.add(Double.toString(n)); 
                    input.add(Double.toString(B)); 
                    input.add(Double.toString(G)); 
                    input.add(Double.toString(width)); 
                    
                }else if("Variance(Population)".equals(methodName)){
                    double XMinusMean = Double.parseDouble(textInput[0].getText());
                    double n = Double.parseDouble(textInput[1].getText());
                    value = XMinusMean / n;
                    
                    input.add(Double.toString(XMinusMean));
                    input.add(Double.toString(n));
                    
                }else if("Variance(Sample)".equals(methodName)){
                    double FX2 = Double.parseDouble(textInput[0].getText());
                    double FXpow2 = Double.parseDouble(textInput[1].getText());
                    double n = Double.parseDouble(textInput[2].getText());
                    value = (n*FX2 - FXpow2) / (n * (n - 1));
                    
                    input.add(Double.toString(FX2));
                    input.add(Double.toString(FXpow2));
                    input.add(Double.toString(n));
                }else{
                    throw new Exception("Error!");
                }
                
                BigDecimal roundedNumber = BigDecimal.valueOf(value).setScale(4, RoundingMode.HALF_UP);
                resultText.setText(roundedNumber.toString() );
                //call super class method to record the content to file
                super.historyRecord(methodName,input, resultText.getText(),"Grouped Data Statistics");
                    
            }catch (ArithmeticException ex) {
                resultText.setText("Divide by zero");
            }catch(NumberFormatException e){
                resultText.setText(errorPrompt);
            }catch(IllegalArgumentException en){
                resultText.setText(en.getMessage());

            }catch (Exception em){
                resultText.setText(em.getMessage());
            }
    }
    
    //This  method record the history calculation when user input value in data container
    @Override
    public void historyRecord(ArrayList<String>input){
        
        String[] arr = input.toArray(String[]::new);
        history.addWord(arr);//call the method in History class
    }
    
    
}

