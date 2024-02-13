
package main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

//This class will perform arithmetic calculation and superclass for other calculation
public class Arithmetic extends Calculator{
    
    //methodLayout will be used as layout for most calculation since it will be derived to other class
    protected GridPane methodLayout = new GridPane();
    private double result;
    private ArrayList<Double> num = new ArrayList<>();
    protected TextField numDecimal = new TextField();
    
    
    //Create contructor with argument
    public Arithmetic(StackPane root) {
        createArithmethicLayout(root);
    }
    //Create default contructor
    public Arithmetic(){
        
    }
    //This method perform addition and return double result
    public double add(String data[]) {
        
        toDouble(data);
        for(int i = 0; i < num.size(); i++){
           result += num.get(i) ;
        }
        return result;
    }
    //This method perform subtraction and return double result
    public double subtract(String data[]) {
        toDouble(data);
        result = num.get(0) - num.get(1);
        return result;
    }  
    //This method perform multiplication and return double result
    public double multiply(String data[]) {
        toDouble(data);
        result = 1;
        for(int i = 0; i < num.size(); i++){
           result *= num.get(i) ;
        }
        return result;
    }
    //This method perform division and return double result
    public double divide(String data[]) throws ArithmeticException{
        toDouble(data);
        result =  num.get(0) / num.get(1) ;
        return result;
    }
    //This method perform exponent and return double result
    public double power(String data[]){
        toDouble(data);
        result = Math.pow(num.get(0) ,num.get(1));
        return result;
    }
    //This method perform factorial and return double result
    public double factorial(int n) {
        //the number can't be negative for factorual
        if (n < 0) {
            throw new IllegalArgumentException("Input cannot be negative.");
        }
        //Our program accept number until 170 because of limited space of text field 
        if(n>170){
            throw new IllegalArgumentException("Max number :170");
        }
        if (n == 0 || n == 1) {
            return 1;
        }
        result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
    //This method create the layout for arithmetic page scene
    private void createArithmethicLayout(StackPane root){
        //Create the main layout
        VBox layout = new VBox();
        //Create text instruction for user to input decimal place
        Text descDecimal = new Text("Set decimal place of the output");
        descDecimal.setFont(Font.font("Segoe Print",14));
        HBox decimalLayout = new HBox();
        decimalLayout.getChildren().addAll(numDecimal,descDecimal);
        decimalLayout.setSpacing(10);
        decimalLayout.setStyle("-fx-control-inner-background: lightblue;");        layout.getChildren().addAll(decimalLayout);
        decimalLayout.setTranslateX(75);
        decimalLayout.setTranslateY(100);
        //Set default value of decimal place
        numDecimal.setText("0");

        //Create all method in arithmetic category
        createMethodGroup("Addition",2,new String [] {" ", "+"},0,0);
        createMethodGroup("Subtraction",2,new String [] {" ", "-"},1,0);
        createMethodGroup("Multiplication",2,new String [] {" ", "x"},2,0);
        createMethodGroup("Division",2,new String [] {" ", "\u00F7"},0,1);
        createMethodGroup("Exponent",2,new String [] {" ", "^"},1,1);
        createMethodGroup("Factorial",1,new String [] {""},2,1);
        //add methodLayout that consist of group of method to root
        root.getChildren().addAll(layout,methodLayout);
        methodLayout.setTranslateY(200);
        methodLayout.setTranslateX(50);
        methodLayout.setHgap(15);
        methodLayout.setVgap(15); 
    }
    //This method perform calculation for arithmetic methods and show the result of the page
    public void performCalculation(String methodName,TextField []textInput,TextField resultText){
        //Clear any previous result and input    
        clearListInput();
            double value = 0.0;
            //Set message when error detected
            String errorPrompt = "Invalid input/decimal place";
            try{
                //Select method that user choose to execute
                switch (methodName) {
                    case "Addition" -> value = add(new String[]{textInput[0].getText(),textInput[1].getText()});
                    case "Subtraction" -> value = subtract(new String[]{textInput[0].getText(),textInput[1].getText()});
                    case "Multiplication" -> value = multiply(new String[]{textInput[0].getText(),textInput[1].getText()});
                    case "Division" -> value = divide(new String[]{textInput[0].getText(),textInput[1].getText()});
                    case "Exponent" -> value = power(new String[]{textInput[0].getText(),textInput[1].getText()});
                    case "Factorial" -> {
                        int n = Integer.parseInt(textInput[0].getText());
                        textInput[0].setText((Integer.toString(n)));
                        value = factorial(n);
                    }
                    default -> {
                    }
                }
                    
                //Round the result based on decimal place
                int decimal = Integer.parseInt(numDecimal.getText());
                BigDecimal roundedNumber = BigDecimal.valueOf(value).setScale(decimal, RoundingMode.HALF_UP);
                //Show result on result text field
                resultText.setText(roundedNumber.toString() );
                //Record the calculation and result
                historyRecord(methodName,textInput[0].getText(),textInput[1].getText(), resultText.getText());
             
            }catch(NumberFormatException e){
                resultText.setText(errorPrompt);

            }catch (ArithmeticException ex) {
                resultText.setText("Divide by zero");

            }catch(IllegalArgumentException en){
                resultText.setText(en.getMessage());

            }catch (Exception em){
                System.out.println(em.getMessage());
            }
    }
    
    //This method convert string value(from text field) to double to use for calculation
    public void toDouble(String data[]){
        for (String data1 : data) {
            double n = Double.parseDouble(data1);
            num.add(n);
        }
    }
    //This method clear the input and result variables
    public void clearListInput(){
        num.clear();
        result = 0;
    }
    //This method create a group of method depends on value passed into it
    //It also will be use in other subclasses
    protected void createMethodGroup(String methodName,int numTextField,String[] nameVariable,int column,int row ){
        //Create the result button and text
        Button resultBtn = new Button("=");
        TextField resultText = new TextField();
        //Create the array of textfield
        TextField[] textInput = new TextField[numTextField];
        //Create the array of label. This label for input text field
        Label[] label = new Label[numTextField];
        //The component of method will be in grid pane layout
        GridPane group = new GridPane();
        //Create label name of the method
        Label name = new Label(methodName);
        name.setFont(Font.font("Segoe Print",14));
        group.add(name, 1, 0);
        
        resultText.setPromptText("Result");
        resultText.setStyle("-fx-control-inner-background: lightblue;");
        setAnimationButton(resultBtn);
        editButtonStyle(resultBtn,14);
        
        //Create instance of textfield(methods might have different input needed to do calculation)
        for(int i = 0; i < numTextField; i++){
            textInput[i] = new TextField();
            textInput[i].setPromptText("Input #" + (i+1));
            textInput[i].setStyle("-fx-control-inner-background: lightblue;");
            textInput[i].setPrefWidth(200);
            label[i] = new Label(nameVariable[i]);
            label[i].setFont(Font.font("Segoe UI Semibold",14));
        }
       
        group.setPrefWidth(280);
        //Add the instance that have been created to grid pane
        int j;
        for(j =0; j < numTextField;j++){

            group.add(label[j], 0, j+1);
            group.add(textInput[j], 1, j+1);
        }
        //Add the result button and text
        group.add(resultBtn, 0, j+1);
        group.add(resultText, 1, j+1);
        group.setVgap(10);

        //Add the group inside the method layout 
        methodLayout.add(group, column, row);
        //If user clicks the result button it will call the method to do calculation
        // and show the result
        resultBtn.setOnAction(event1 ->{
            performCalculation(methodName,textInput,resultText);
        });
    }
    //This method will record history calculaton for arithmetic
    public void historyRecord(String methodName, String input1,String input2, String result){
        
        String operation = "";
        switch (methodName) {
            case "Addition" -> operation = "+";
            case "Subtraction" -> operation = "-";
            case "Multiplication" -> operation = "x";
            case "Division" -> operation = "\u00F7";
            case "Factorial" -> operation = "!";
            default -> {
            }
        }
        
        String[] save;
        if(methodName.equals("Factorial")){
            save = new String[] {"Arithmetic - " + methodName ,"> " + input1 +" "+ operation + " = " + result};
        }else{
            save = new String[]{"Arithmetic - " + methodName,"> " + input1 +" "+ operation +" "+ input2 + " = " + result};
        }
        history.addWord(save);//call the method in History class
    }
}