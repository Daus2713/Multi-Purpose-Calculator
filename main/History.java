
package main;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

//The class record all the valid calculation and result in the program
public class History {
    
    private String filename = "Data.txt"; // File name for storing the history
    private File file;
    private final int MAX_LINES = 1000; // Maximum number of lines to keep in the history file
    
    // contructor that initialize the file object
    public History() {
        //User directory user.home. Different OS have different directory
        this.file = new File(System.getProperty("user.home"), filename);
    }
    //This method read words from file and creat new file if it not exists
    //Return word to be display later
    public List<String> readWords() throws FileNotFoundException {

        List<String> words = new ArrayList<>();

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(History.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            words.add(scanner.nextLine());
        }

        scanner.close();

        return words;
    }
    //This method add new word into the file
    public void addWord(String[] words) {
        try {
            List<String> existingWords = readWords();
            //If the existing words exist the max line(1000)
            if (existingWords.size() >= MAX_LINES) {
                // Remove the first lines to keep only 1000 lines
                existingWords.subList(0, existingWords.size() - MAX_LINES).clear();
            }
            //Add date and time to saved calculation
            LocalDateTime currentTime = LocalDateTime.now();
            String timeStamp = currentTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss"));
            //Copy the existing word, add the time stamp and add the new word 
            existingWords.add("\n" + timeStamp);
            existingWords.addAll(Arrays.asList(words));
            
            PrintWriter writer = new PrintWriter(file);
            //write it back to file
            for (String w : existingWords) {
                writer.println(w);
            }

            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(History.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //This method delete all the content inside file
    private void deleteContent() {
        try {
            PrintWriter writer = new PrintWriter(file.getPath());
            writer.print("");//Modify the content to zero string
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(History.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //This method create window to see saved history from file
    public void seeHistory() {
        //Create stage
        Stage historyWindow = new Stage();
        historyWindow.setTitle("History");
        //Create text area to display the content
        TextArea textArea = new TextArea();
        textArea.setEditable(false);//Make it cant editable by user
        //Call method to read word and save it into string builder
        try {
            List<String> words = readWords();
            StringBuilder contentBuilder = new StringBuilder();
            for (String word : words) {
                contentBuilder.append(word).append("\n");
            }
            textArea.setText(contentBuilder.toString());//SEt into the text area
        } catch (FileNotFoundException e) {
            textArea.setText("File not found.");
        }

        // Create menu bar
        MenuBar menuBar = new MenuBar();

        // Create File menu
        Menu fileMenu = new Menu("File");

        // Create Delete option
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> {
            deleteContent();
            textArea.setText("");
        });

        // Create Export option
        MenuItem exportItem = new MenuItem("Export");
        exportItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Text File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File fileHist = fileChooser.showSaveDialog(historyWindow);
            if (fileHist != null) {
                try (PrintWriter writer = new PrintWriter(fileHist)) {
                    writer.write(textArea.getText());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        // Create Directory option
        MenuItem directoryItem = new MenuItem("Directory");
        directoryItem.setOnAction(e -> {
            String directory = file.getParent();
            openFileDirectory(directory);
        });

        // Add options to the File menu
        fileMenu.getItems().addAll(deleteItem, exportItem, directoryItem);
        // Add the File menu to the menu bar
        menuBar.getMenus().add(fileMenu);
        //Show text when the file has no saved history
        Label noHistoryLabel = new Label("No history");
        noHistoryLabel.setStyle("-fx-font-size: 18px;");

        StackPane.setAlignment(noHistoryLabel, Pos.CENTER);
        noHistoryLabel.visibleProperty().bind(Bindings.isEmpty(textArea.textProperty()));

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        StackPane layout = new StackPane(menuBar, scrollPane, noHistoryLabel);
        menuBar.setTranslateX(0);
        menuBar.setTranslateY(-190);
        layout.getChildren().get(0).toFront();
        Scene scene = new Scene(layout, 400, 400);
        historyWindow.setScene(scene);
        //Add icon image for see history window
        Image icon = new Image(getClass().getResource("Images/icon.png").toString());
        historyWindow.getIcons().add(icon);
        // Show the history window
        historyWindow.show();

        // Scroll to the bottom of the text area to show the latest text
        textArea.positionCaret(textArea.getText().length());
    }
    //This method open the file explorer and show the file directory
    private void openFileDirectory(String directory) {
        if (Desktop.isDesktopSupported()) {
            try {
                File directoryFile = new File(directory);
                Desktop.getDesktop().open(directoryFile);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

