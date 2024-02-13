
package main;
/* This program created by students of IIUM from Kuliyyah of Information and Communicaton Technology
 * Our group members: Brother Firdaus(2211901), Brother Hafizuddin(2217261), Brother Amir(2214783)
 * Brother Hafiz(2217477), Brother Danial(2216993) from couse Object-Oriented Programming
 * Section 5 Sem 2, 22/23
*/

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

//Driver class for this program
public class Calculator extends Application {

    // Declare important variables and instance that 
    // need in this class and used by subclasses
    protected ScrollPane container;
    private Image[] formula;
    private ImageView[] formulaview;
    private Image icon;
    private String titleProgram = "Multi-Purpose Calculator";
    private Stage stage;
    private Button titlePage;
    private Calculator startPageCategory;
    private Text pageName;
    protected Button backButton;
    protected Button titleButton;
    protected StackPane root;
    protected History history = new History();
    protected Button seeHistory;
    protected Image backgroundImage;
    protected ImageView backgroundView;


    
    //The main entry point of the applicaton
    public static void main(String[] args) {
        launch(args);
    }
    //Start the JavaFX application
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        //create root node as a StackPane. root will use to as main container of most of the scene/page that will be created later
        root = new StackPane();
        //add root node to ScrollPane node to add scroll functions to root 
        container = new ScrollPane(root);
        container.setFitToWidth(true);
        container.setFitToHeight(true);
        container.vvalueProperty().bind(root.heightProperty());
        container.setContent(root);
        container.setMinSize(995, 645); // Set the minimum width and height
        root.setMinSize(995, 645); // Set the minimum width and height

        //create back button for user to go back to previous page
        backButton = new Button("Go Back");
        backButton.setTranslateX(20);
        backButton.setTranslateY(10);
        
        //create title button for user to go to title page
        titleButton = new Button("Title");
        titleButton.setTranslateX(120);
        titleButton.setTranslateY(10);
        
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setAlignment(titleButton, Pos.TOP_LEFT);
        
        //Set a background image for the program
        backgroundImage = new Image(getClass().getResource("Images/background.png").toString());
        BackgroundImage backgroundPic = new BackgroundImage(
                backgroundImage,
                null,    
                null,   
                null,    
                null      
        );

        // Create a background with the image
        Background background = new Background(backgroundPic);

        // set background image to root node
        root.setBackground(background);
        
        // Set the initial background size to maintain aspect ratio
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();
        double imageWidth = backgroundPic.getImage().getWidth();
        double imageHeight = backgroundPic.getImage().getHeight();
        double scaleFactor = Math.max(screenWidth / imageWidth, screenHeight / imageHeight);
        BackgroundSize backgroundSize = new BackgroundSize(
                imageWidth * scaleFactor,
                imageHeight * scaleFactor,
                false,
                false,
                false,
                false);
        root.setBackground(new Background(new BackgroundImage(
                backgroundPic.getImage(),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize)));
    
        
        //call method to create the first page of program
        createTitlePage();
        //Set icon image of the program
        icon = new Image(getClass().getResource("Images/icon.png").toString());
        stage.getIcons().add(icon);
        //Create window with 1280 x 720 aspect ration
        Scene initialScene = new Scene(container, 1280, 720);
        stage.setScene(initialScene);
        stage.setTitle(titleProgram);
        stage.setMaximized(true);
        stage.show();
    }
    
   
    //This method will create the layout of the first page or title page of the program
    protected void createTitlePage() {
        
        //mainLayout is the main container of the nodes that will be used in title page
        StackPane mainLayout = new StackPane();
        //Create components like text to show title, button to show options available on title page
        Button calcButton = new Button("Proceed to calculation menu");
        seeHistory = new Button("See History");
        calcButton.setFont(Font.font("Segoe Print",15));
        seeHistory.setFont(Font.font("Segoe Print",15));
        //Set image to show name of program
        Image namePage = new Image(getClass().getResource("Images/program name.png").toString());
        ImageView namePageView = new ImageView(namePage);

        namePageView.setTranslateX(0);
        namePageView.setTranslateY(-100);
        calcButton.setTranslateX(0);
        calcButton.setTranslateY(70);
        seeHistory.setTranslateX(0);
        seeHistory.setTranslateY(140);
        calcButton.setStyle("-fx-background-color: lightblue;-fx-border-color: darkgray;-fx-border-width: 1px;");
        seeHistory.setStyle("-fx-background-color: lightblue;-fx-border-color: darkgray;-fx-border-width: 1px;");
        
        //add all nodes to mainLayout
        mainLayout.getChildren().addAll(namePageView, calcButton,seeHistory);
        //add to root node that will show the added node to stage
        root.getChildren().add(mainLayout);
        // Add hover effect to the button and edit style of button
        setAnimationButton(calcButton);
        setAnimationButton(seeHistory);
        editButtonStyle(titleButton,14.0);
        editButtonStyle(backButton,14.0);
        setAnimationButton(titleButton);
        setAnimationButton(backButton);
        
        
        //If user clicks button to show list of calculation
        calcButton.setOnAction(e -> {
            root.getChildren().clear();//delete all children nodes inside root. The mainLayout node removed
            createListMathMethodsPage();//create next page that will show list of mathematical methods 
        });
        //If user clicks button to show record of calculation he has done before
        seeHistory.setOnAction(e->{
            history.seeHistory();//call method seeHistory in class History to show the record
        });
        //If user clikcs button to go back to title page
        titleButton.setOnAction(ef ->{
           root.getChildren().clear();
           createTitlePage();
        });
        
    }
    //This method add enlarge size animation to button
    public void setAnimationButton(Button button){
        button.setOnMouseEntered(event -> {
            button.setScaleX(1.2);
            button.setScaleY(1.2);
        });

        button.setOnMouseExited(event -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });
        
    }
    //This method edit the text style to "Segoe Print"
    public void editTextStyle(Text text,String font,double size){
        text.setFont(Font.font("Segoe Print",size));
        
    }
    //This method create a page that will show list of calculation category available for our program
    protected void createListMathMethodsPage() {
        //Create text page title 
        Text txt = new Text("List of Calculation");
        txt.setTranslateY(10);
        txt.setFill(Color.BROWN);
        editTextStyle(txt,"Segoe Print",40);

        //Buttons for user to choose calculation he want to do
        Button arithmeticPage = new Button(">");
        Button probabilityPage = new Button(">");
        Button indivStatsPage = new Button(">");
        Button groupStatsPage = new Button(">");
        
        //Create array of image and imageview
        formula = new Image[4];
        formulaview = new ImageView[4];
        //Create object that refer to certain image formula that will show detail of each category
        formula[0] = new Image(getClass().getResource("Images/formula1.png").toString());
        formulaview[0] = new ImageView(formula[0]); 
        formula[1] = new Image(getClass().getResource("Images/formula2.png").toString());
        formulaview[1] = new ImageView(formula[1]); 
        formula[2] = new Image(getClass().getResource("Images/formula3.png").toString());
        formulaview[2] = new ImageView(formula[2]); 
        formula[3] = new Image(getClass().getResource("Images/formula4.png").toString());
        formulaview[3] = new ImageView(formula[3]); 
        
        //Create layout for each category, consists of category text, button to create page and imageView to show the detail for each category
        HBox arithmeticMethods = createMethodBox("Arithmetic",arithmeticPage, formulaview[0]);
        HBox probabilityMethods = createMethodBox("Probability" ,probabilityPage, formulaview[1]);
        HBox individualMethods = createMethodBox("Individual Data",indivStatsPage, formulaview[2]);
        HBox groupedMethods = createMethodBox("Grouped Data",groupStatsPage, formulaview[3]);
        
        //Create text for statistics, Statistics consists of two type calculation, Individual and Grouped data
        Text stats = new Text("Statistics : ");
        stats.setFont(Font.font("Segoe Print",25));
        stats.setFill(Color.DARKBLUE);
        stats.setTranslateX(50);
        //Create vbox layout to add nodes of category calculation in statistics
        VBox statisticsMethods = new VBox(stats,individualMethods,groupedMethods);
        statisticsMethods.setSpacing(10);
        //Create second layout to put all nodes that refers to each category calculation
        VBox secondLayout = new VBox();
        secondLayout.getChildren().addAll(arithmeticMethods, probabilityMethods, statisticsMethods);
        secondLayout.setTranslateX(50);
        secondLayout.setTranslateY(100);
        secondLayout.setSpacing(5);
        //Create main layout for this page, consist of back button, page title text and layout that contains all calculation nodes
        StackPane layout = new StackPane(txt, titleButton, secondLayout);
        StackPane.setAlignment(txt, Pos.TOP_CENTER);
        StackPane.setAlignment(secondLayout, Pos.CENTER_LEFT);
        //add the main layout for this page to root node
        root.getChildren().add(layout);
        seeHistory.setTranslateX(-20);
        seeHistory.setTranslateY(10);
        StackPane.setAlignment(seeHistory, Pos.TOP_RIGHT);
        
        //Go back to title page when back button clicked
        titleButton.setOnAction(e -> {
            root.getChildren().clear();//remove main layout for this page. remove all nodes created in this method
            createTitlePage();//create again the title page
        });
        //If user choose to see arithmetic calculation
        arithmeticPage.setOnAction(e -> {
            root.getChildren().clear();
            createArithmeticPage();//call method to create arithmetic page
        });
        //If user choose to see probability calculation
        probabilityPage.setOnAction(e -> {
            root.getChildren().clear();
            createProbabilityPage();//call method to create probability page
        });
        //If user choose to see individual data statistics calculation
        indivStatsPage.setOnAction(e->{
            root.getChildren().clear();
            createIndivStatsPage();//call method to create individual data page
        });
        //If user choose to see grouped data statistics calculation
        groupStatsPage.setOnAction(e->{
            root.getChildren().clear();
            createGroupStatsPage();//call method to create grouped data page
        });  
    }
    

    //This method edit the style of button
    public void editButtonStyle(Button button,double size){

        if(button.getText().equals("="))
            button.setFont(Font.font("Segoe UI Semibold",size));
        else
            button.setFont(Font.font("Segoe Script",size));
            
        button.setStyle("-fx-background-color: transparent;-fx-border-color: darkgray;-fx-border-width: 1px;");
        button.setStyle("-fx-background-color: transparent;-fx-border-color: darkgray;-fx-border-width: 1px;");

    }
    // This method will help to build the layout for each category calculation
    private HBox createMethodBox(String methodName, Button nextScene, ImageView formula) {
        
        Text methodText = new Text(methodName);
        //Add style to category text
        if ("Individual Data".equals(methodName) || "Grouped Data".equals(methodName)) {
            methodText.setFont(Font.font("Segoe Script",22));
            methodText.setFill(Color.BLUEVIOLET);
        } else {
            methodText.setFont(Font.font("Segoe Print",25));
            methodText.setFill(Color.DARKBLUE);
        }
        //Add button to show detail or methods inside that category
        Button methodInfo = new Button("?");
        editButtonStyle(methodInfo,14);
        editButtonStyle(nextScene,14);
        setAnimationButton(nextScene);
        //Use tooltip to show the details
        Tooltip tooltip = new Tooltip();
        tooltip.setGraphic(formula);
        Tooltip.install(methodInfo, tooltip);
        tooltip.setShowDelay(Duration.millis(150));

        //Group each category inside box
        HBox methodBox;
        methodBox = new HBox(methodText, methodInfo, nextScene);
        methodBox.setSpacing(10);

        // Set fixed width and height for each method box
        methodBox.setMinSize(300, 80);
        methodBox.setMaxSize(300, 80);
        methodBox.setAlignment(Pos.CENTER);
        // Add hover effect to the method box
        methodBox.setOnMouseEntered(event -> {
            methodBox.setStyle("-fx-background-color: lightblue;");
        });
        methodBox.setOnMouseExited(event -> {
            methodBox.setStyle("-fx-background-color: transparent;");
        });
        // Limit the size of the formula/detail tooltip image
        formula.setPreserveRatio(true);
        formula.setFitWidth(400);
        formula.setFitHeight(400);
        return methodBox;
    }

    //This method creates arithmetic page calculation
    private void createArithmeticPage() {
        //create object arithmethic to start create layout 
        startPageCategory = new Arithmetic(root);
        pageName = new Text("Arithmetic");
        editTextStyle(pageName,"Segoe Print",40);
        StackPane.setAlignment(pageName, Pos.TOP_CENTER);
        //add back button and title button to arithmetic page
        root.getChildren().addAll(backButton,titleButton,seeHistory,pageName);
            
        backButton.setOnAction(en -> {
           root.getChildren().clear();
           createListMathMethodsPage();
        });
    }
    //This method creates Probability page calculation
    private void createProbabilityPage() {
        startPageCategory = new Probability(root);
        
        pageName = new Text("Probability");
        editTextStyle(pageName,"Segoe Print",40);
        StackPane.setAlignment(pageName, Pos.TOP_CENTER);
        
        //add back button and title button to probability page
        root.getChildren().addAll(backButton,titleButton,seeHistory,pageName);
            
        backButton.setOnAction(en -> {
           root.getChildren().clear();
           createListMathMethodsPage();

        });
    }
    //This method creates Individual statistics page calculation
    private void createIndivStatsPage(){
        
        startPageCategory = new IndividualDataStatistics(root);
        pageName = new Text("Individual Data ");
        editTextStyle(pageName,"Segoe Print",40);
        StackPane.setAlignment(pageName, Pos.TOP_CENTER);
        //add back button and title button to individual statistics  page
        root.getChildren().addAll(backButton,titleButton,seeHistory,pageName);
        backButton.setOnAction(en -> {
           root.getChildren().clear();
           createListMathMethodsPage();

        });

    }
    //This method creates Grouped statistics page calculation
    private void createGroupStatsPage(){
        
        startPageCategory = new GroupedDataStatistics(root);
        pageName = new Text("Grouped Data");
        editTextStyle(pageName,"Segoe Print",40);
        StackPane.setAlignment(pageName, Pos.TOP_CENTER);
        //add back button and title button to grouped statistics page
        root.getChildren().addAll(backButton,titleButton,seeHistory,pageName);
        backButton.setOnAction(en -> {
           root.getChildren().clear();
           createListMathMethodsPage();

        });
    }
}
