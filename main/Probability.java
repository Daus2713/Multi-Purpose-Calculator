package main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

//The class calculate probability methods
public class Probability extends Arithmetic{
   //Create contructor with argument
    public Probability (StackPane root){
        createProbabilityPage(root);
    }
    //This method calculate permutation and return result in double
    private double calculatePermutation(int n, int r) {
        if (n < r) {
            return 0;
        }
        double numerator = factorial(n);
        double denominator = factorial(n - r);
        return numerator / denominator;
    }
    //This method calculate combination and return result in double
    private double calculateCombination(int n, int r) throws Exception {
        if (n < r) {
            throw new Exception("n must be greater or equal to X");
        }

        double numerator = factorial(n);
        double denominator = factorial(r) * factorial(n - r);

        return numerator / denominator;
    }
    //This method calculate binomial distribution and return result in double
    private double calculateBinomialDistribution(int n, int r, double p) throws Exception {
        if (n < r || p < 0 || p > 1) {
            return 0;
        }

        double coefficient = calculateCombination(n, r);
        double successProbability = Math.pow(p, r);
        double failureProbability = Math.pow(1 - p, n - r);

        return coefficient * successProbability * failureProbability;

    }
    //This method calculate Poisson distribution and return result in double
    private double calculatePoissonDistribution(double lambda, int r) {
        if (lambda <= 0 || r < 0) {
            return 0;
        }

        return (Math.pow(Math.E, -lambda) * Math.pow(lambda, r)) / factorial(r);
    }
    //This method calculate Geometric Distribution and return result in double
    private double calculateGeometricDistribution(double probability, int numTrials) {
        if (probability < 0 || probability > 1) {
            throw new IllegalArgumentException("Probability must be between 0 and 1.");
        }

        if (numTrials < 0) {
            throw new IllegalArgumentException("Number of trials must be a non-negative integer.");
        }

        return Math.pow(1 - probability, numTrials - 1) * probability;
    }
    //This method create the layout for probability page scene
    private void createProbabilityPage(StackPane root) {
        
        methodLayout.setTranslateY(100);
        methodLayout.setTranslateX(30);
        methodLayout.setHgap(15);
        methodLayout.setVgap(15);
        
        createMethodGroup("Combination",2,new String[]{"n","X"},0,0);
        createMethodGroup("Permutation",2,new String[]{"n","X"},1,0);
        createMethodGroup("Binomial Distribution",3,new String[]{"n","p","X"},2,0);;
        createMethodGroup("HyperGeometric Distribution",4,new String[]{"a","b","X","n"},0,1);
        createMethodGroup("Poisson Distribution",2,new String[]{"\u03BB","X"},1,1);
        createMethodGroup("Geometric Distribution",2,new String[]{"n","p"},2,1);
        root.getChildren().addAll(methodLayout);

    }
    //This method override the method in arithmetic class. perform same function but for probability
    @Override
    public void performCalculation(String methodName,TextField []textInput,TextField resultText){
            double value = 0.0;
            String errorPrompt = "Invalid input!";
            ArrayList<String> input = new ArrayList<>();

            try{
                
                if("Combination".equals(methodName)){
                    int n = Integer.parseInt(textInput[0].getText());
                    int X = Integer.parseInt(textInput[1].getText());
                    value = calculateCombination(n,X);
                    
                    input.add(Double.toString(n));
                    input.add(Double.toString(X));
                }
                else if("Permutation".equals(methodName)){
                    int n = Integer.parseInt(textInput[0].getText());
                    int r = Integer.parseInt(textInput[1].getText());
                    value = calculatePermutation(n, r);
                    
                    input.add(Double.toString(n));
                    input.add(Double.toString(r));
                    
                }
                else if("Binomial Distribution".equals(methodName)){
                    int n = Integer.parseInt(textInput[0].getText());
                    int X = Integer.parseInt(textInput[2].getText());
                    double p = Double.parseDouble(textInput[1].getText());
                    value = calculateBinomialDistribution(n, X, p);
                    
                    input.add(Double.toString(n));
                    input.add(Double.toString(p));
                    input.add(Double.toString(X));
                }
                else if("HyperGeometric Distribution".equals(methodName)){
                    int a = Integer.parseInt(textInput[0].getText());
                    int b = Integer.parseInt(textInput[1].getText());
                    int X = Integer.parseInt(textInput[2].getText());
                    int n = Integer.parseInt(textInput[3].getText());
                    double comb1 = calculateCombination(a,X);
                    double comb2 = calculateCombination(b,n-X);
                    double comb3 = calculateCombination(a+b,n);
                    value = (comb1 * comb2) / comb3;
                    
                    input.add(Double.toString(a));
                    input.add(Double.toString(b));
                    input.add(Double.toString(X));
                    input.add(Double.toString(n));
                    
                }
                else if("Poisson Distribution".equals(methodName)){
                    double lambda = Double.parseDouble(textInput[0].getText());
                    int r = Integer.parseInt(textInput[1].getText());
                    value = calculatePoissonDistribution(lambda,r);
                    
                    input.add(Double.toString(lambda));
                    input.add(Double.toString(r));
                }
                else if("Geometric Distribution".equals(methodName)){
                    double p = Double.parseDouble(textInput[1].getText());
                    int n = Integer.parseInt(textInput[0].getText());
                    value = calculateGeometricDistribution(p, n);
                   
                    input.add(Double.toString(n));
                    input.add(Double.toString(p));
                  
                }else{
                    throw new Exception("Error!");
                }
                
                BigDecimal roundedNumber = BigDecimal.valueOf(value).setScale(4, RoundingMode.HALF_UP);
                resultText.setText(roundedNumber.toString() );
                historyRecord(methodName,input, resultText.getText());
                    
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
    
    //This method record the history calculation when user input value in method box  
    public void historyRecord(String methodName, ArrayList<String>input,String result){
        
        ArrayList<String> save = new ArrayList<>();
         save.add("Grouped Data Statistics" + " - " + methodName);
        int i = 0;
        for(;i < input.size();i++){
            save.add("Input #" + (i+1) + " : " + input.get(i));
        }
        save.add("Result : " + result);
        String[] arr = save.toArray(String[]::new);
        history.addWord(arr);//call the method in History class 
    }   
}