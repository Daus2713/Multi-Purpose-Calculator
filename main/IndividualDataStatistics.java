
package main;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

//This class perform individual data statistics.
//It has two way to show result. From value inputted in method box and from data inputted in data container
public class IndividualDataStatistics extends Arithmetic{

    //Create instance to collect input and set the output to 4 decimal place
    List<String> data = new ArrayList<>();
    private final int decimal = 4;
    //Create contructor with argument
    public IndividualDataStatistics(StackPane root){
        createIndivStatsLayout(root);
    }
    //Create default contructor
    public IndividualDataStatistics(){
        
    }
    //This method find the mode(s) based on inputted data. Return boolean indicate the presence of mode in the data
    private boolean mode(double[] num, ArrayList<Double> mode) {
        int maxCount = 0;
        int modeCount = 0;

        // Calculate the frequency of each number
        for (int i = 0; i < num.length; i++) {
            int count = 0;
            for (int j = 0; j < num.length; j++) {
                if (num[j] == num[i]) {
                    count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                modeCount = 1;
            } else if (count == maxCount) {
                modeCount++;
            }
        }

        // If there is no mode, return false
        if (maxCount == 1) {
            return false;
        }

        // Store the modes in the mode array
        int index = 0;
        for (int i = 0; i < num.length; i++) {
            int count = 0;
            for (int j = 0; j < num.length; j++) {
                if (num[j] == num[i]) {
                    count++;
                }
            }
            if (count == maxCount) {
                mode.add(num[i]);
                index++;
            }
        }
        //remove repeated mode in arraylist
        for (int i = 0; i < mode.size(); i++) {
            Double currentNumber = mode.get(i);
            for (int j = i + 1; j < mode.size(); j++) {
                if (currentNumber.equals(mode.get(j))) {
                    mode.remove(j);
                    j--; // Decrement j to recheck the same index
                }
            }
        }
        return true;
    }

    //This method calculate mean for individual data and return the double result
    private double mean(double[] num){
        
        double total=0, ans;
        
        for(int i=0; i<num.length; i++){
            total += num[i];
        }
        ans = total/num.length;
        
        return ans;
    }
    

    //This method calculate median for individual data and return the double result
    private double median(double[] num) {
        // Sort the data in ascending order
        Arrays.sort(num);

        int length = num.length;
        double median;
        // Check if the number of elements is odd
        if (length % 2 != 0) {
            median = num[length / 2];
        } else {
            // If the number of elements is even, average the two middle values
            double middle1 = num[length / 2 - 1];
            double middle2 = num[length / 2];
            median = (middle1 + middle2) / 2;
        }
        return median;
    }
    //This method calculate population variance for individual data and return the double result
    private double populationVariance(double[] num, double mean){
        
        double total=0, ans;
        for(int i=0; i<num.length; i++){
            total += Math.pow((num[i] - mean),2);
        }
        ans = total / num.length;
        return ans;
    }
    
    //This method calculate mean for sample variance for individual data and return the double result
    private double sampleVariance(double[] num ){
        
        double total1=0, total2=0, ans;
        double size = num.length;
        for(int i=0; i<num.length; i++){
            total1 += num[i];
        }
        for(int j=0; j<num.length; j++){
            total2 += (num[j] * num[j]) ;
        }      
        ans = ( (size * total2)-(total1 * total1) ) / (size * (size-1));
        return ans;
    }

    //This method create the layout for individual data page scene
    private void createIndivStatsLayout(StackPane root){
        //create the main layout
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setTranslateX(30);
        vbox.setTranslateY(90);
        //Create text area for user to input data
        TextArea textArea = new TextArea();
        //Create text to show data counted in text area
        Text countData = new Text("");
        editTextStyle(countData,"Segoe Print",17);
        //call method to create data container 
        createDataContainer(textArea);
        //create sub layout to arrange the nodes inside the main layout
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        //this sublayou will show the top side of scene
        hbox1.getChildren().addAll(textArea,countData);
        countData.setVisible(false); // Make the text invisible when the scene created
        hbox1.setSpacing(10);
        textArea.setStyle("-fx-control-inner-background: lightblue;");
        //Add button for user to confirm the data inputted
        Button confirmButton = new Button("Confirm");
        //Make the program focus on button when the scene just created
        Platform.runLater(() -> confirmButton.requestFocus());
        //Add button to show data summary based on the data inputted
        Button showDataSummary = new Button("Show Data Summary");
        showDataSummary.setDisable(true);
        //This will show below the layout of data container
        hbox2.getChildren().addAll(confirmButton,showDataSummary);
        hbox2.setSpacing(15);
        
        setAnimationButton(confirmButton);
        editButtonStyle(confirmButton,10);
        setAnimationButton(showDataSummary);
        editButtonStyle(showDataSummary,10);
        //If clicks confirm button
        confirmButton.setOnAction(event -> {
            data.clear();//clear previous data inputted user that stored in data instance
            showDataSummary.setDisable(true);//Make it disable(for error detection purpose)
            hbox1.getChildren().remove(countData);//Remove the previous text of count data
            //check data if it valid or not and count the data
            boolean validData = checkDataProperties(textArea,hbox1,countData);
            countData.setVisible(true);//If no error, the count data become visible
            if(validData)
                showDataSummary.setDisable(false);//The button can be use after data error detection
        });
        //If user clicks the button to see summary of data
        showDataSummary.setOnAction(event ->{
            calcSummaryData();
            showDataSummary.setDisable(true);
        });

        //Add sublayout to main layout
        vbox.getChildren().addAll(hbox1, hbox2);
        //Create the group of methods for individual data statistics
        createMethodGroup("Mean,\u00B5(Sample/Population)",2,new String[]{"\u2211 X","N"},0,0);
        createMethodGroup("Mode(Individual Data)",0,new String[]{},1,0);
        createMethodGroup("Median(Individual Data)",0,new String[]{},2,0);
        createMethodGroup("Variance(Population)",2,new String[]{"\u2211 (X - \u00B5)","n"},3,0);
        createMethodGroup("Variance(Sample)",3,new String[]{"\u2211 X ^ 2", "(\u2211 X)^2","n"},0,1);
        methodLayout.setStyle(" -fx-background-radius: 30;-fx-font-size: 13px;");
        //add method layout and main layout to root node
        root.getChildren().addAll(vbox,methodLayout);
        methodLayout.setTranslateY(200);
        methodLayout.setTranslateX(30);
        methodLayout.setHgap(15);
        methodLayout.setVgap(15);
    }
    
    //This method override the method in arithmetic class. perform same function but for individual data
    @Override
    public void performCalculation(String methodName,TextField []textInput,TextField resultText){
            double value = 0.0;
            String errorPrompt = "Invalid input!";
            ArrayList<String> input = new ArrayList<>();//store the input to record history

            try{
                
                if("Mean,\u00B5(Sample/Population)".equals(methodName)){
                    double sumValue = Double.parseDouble(textInput[0].getText());
                    double sampleSize = Double.parseDouble(textInput[1].getText());
                    value = sumValue/sampleSize;
                    input.add(Double.toString(sumValue));
                    input.add(Double.toString(sampleSize));
                }
                else if("Variance(Population)".equals(methodName)){
                    double sumValueMinusMean = Double.parseDouble(textInput[0].getText());
                    double sampleSize = Double.parseDouble(textInput[1].getText());
                    value = Math.pow(sumValueMinusMean, 2) / sampleSize;
                    input.add(Double.toString(sumValueMinusMean));
                    input.add(Double.toString(sampleSize));
                }
                else if("Variance(Sample)".equals(methodName)){
                    double sumValuePow2 = Double.parseDouble(textInput[0].getText());
                    double sumPow2Value = Double.parseDouble(textInput[1].getText());
                    double sampleSize = Double.parseDouble(textInput[2].getText());

                    value = (sampleSize*sumValuePow2 - sumPow2Value) / (sampleSize * (sampleSize - 1));                
                    input.add(Double.toString(sumValuePow2));
                    input.add(Double.toString(sumPow2Value));
                    input.add(Double.toString(sampleSize));

                }else{
                    throw new Exception("Only when data inputted!");
                }
                BigDecimal roundedNumber = BigDecimal.valueOf(value).setScale(decimal, RoundingMode.HALF_UP);
                resultText.setText(roundedNumber.toString() );
                historyRecord(methodName,input, resultText.getText(),"Individual Data Statistics");
                    
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
    //This method create data container layout that use to get data that user inputted
    public void createDataContainer(TextArea textArea){
        //TextArea textArea = new TextArea();
        textArea.setPrefColumnCount(24);
        textArea.setPrefRowCount(3);
        textArea.setPromptText("""
                               Input data here (if you calculate using data).   
                               Seperate different value with space.    
                                  Example: 22.3 26.4 29.0 -> 3 values detected""");
        textArea.setMaxWidth(450);// Set the maximum width to allow wrapping
        textArea.setWrapText(true);
    }
    //This method check data inputted by user. Return boolean to show the validity of data
    public boolean checkDataProperties(TextArea textArea,HBox hbox ,Text countData){

            String text = textArea.getText();
            boolean invalidData = false;
            if (text.trim().isEmpty()) {
                // No data in the field
                countData.setText("Data counted : 0" + "   **Please input the data");
                invalidData = true;
            }else{
                String[] numGet= text.split("\\s+");
                Collections.addAll(data, numGet);

                for (String element : data) {
                    try {
                        Double.valueOf(element);

                    } catch (NumberFormatException e) {
                        // Invalid input
                         countData.setText("Invalid input!");
                        invalidData = true;
                    }
                }
            }
            //If data is valid. Count the data
            if(!invalidData){
                countData.setText("Data counted : " + data.size());
                hbox.getChildren().addAll(countData);
                 return true;
            }else{ // if data is invalid
                hbox.getChildren().addAll(countData);
                return false; 
            }
        }
    //This method calculate summary of data inputted by user and show the result in text field
    protected void calcSummaryData(){
        
        String[] arrayData = data.toArray(String[]::new);
        double[] doubleArray = new double[arrayData.length];
        ArrayList<String> save = new ArrayList<>();
        //Convert string data to double data to use it in calculation
        for (int i = 0; i < arrayData.length; i++) {
            doubleArray[i] = Double.parseDouble(arrayData[i]);
        }
        

        double mean = mean(doubleArray);//find mean
        ArrayList<Double> mode = new ArrayList<>();
        boolean hasMode = mode(doubleArray,mode);//find mode and get know the data has mode or not
        double median = median(doubleArray);//find median
        double popuVariance = populationVariance(doubleArray,mean);//find population variance
        double sampleVariance = sampleVariance(doubleArray);//find  sample variance
        double arraySummary[] = {mean,0.0,median,popuVariance,sampleVariance};//store to array to use for showing result
        
        //add to save array for record history
        save.add("Individual Data Statistics - Data summary");
        String dataSave ="";
        for(String val : data){
            dataSave += val +", ";
        }
        save.add(dataSave);//Record history
        save.add("Mean : " + mean);//Record history
        
        //Index of result text field that has been created in method group method
        int indexResultTxt[] = {6,2,2,6,8};
        for(int i = 0;i < 5;i++){
            //Get the group node that contain the method component 
            GridPane group = (GridPane)(methodLayout.getChildren().get(i));
            //Get the text field result
            TextField txt = (TextField)(group.getChildren().get(indexResultTxt[i]));
            //For mode 
            if(i == 1){
                String modeTxt;
                if(hasMode){ //has mode
                    modeTxt = "Mode:";
                    for(int j = 0; j < mode.size();j++){
                        if(j>0){
                            modeTxt += ",";
                        }
                        modeTxt += (" " + mode.get(j));
                        
                    }
                }else{
                    modeTxt = "No mode" ;
                }
                txt.setText(modeTxt); // set value mode in result text field
                save.add("Mode : " + modeTxt); //Record history
            }else{
                // set other resutl calculation in result text field
                txt.setText(Double.toString(arraySummary[i]));
            }
        }
        //Record history
        save.add("Median : " + median);
        save.add("Population Variance : " + popuVariance);
        save.add("Sample Variance : " + sampleVariance);
        
        //Call method to do record all the string gathered in this method
        historyRecord(save);

    }
    //This method record the history calculation when user input value in method box  
    public void historyRecord(String methodName, ArrayList<String>input,String result,String category){
        
        ArrayList<String> save = new ArrayList<>();
         save.add( category + " - " + methodName);
        int i = 0;
        for(;i < input.size();i++){
            save.add("Input #" + (i+1) + " : " + input.get(i));
        }
        save.add("Result : " + result);
        String[] arr = save.toArray(String[]::new);
        history.addWord(arr);//call the method in History class
        
    }   
    //This overloaded method record the history calculation when user input value in data container
    public void historyRecord(ArrayList<String>input){
        
        String[] arr = input.toArray(String[]::new);
        history.addWord(arr);//call the method in History class
    }   
}

