package com.pharmacy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("ACTIVE PROJECT PATH = " + System.getProperty("user.dir"));
        primaryStage = stage;
        // Initial screen is now the Premium Splash
        setRoot("fxml/Splash.fxml", "💊 Welcome to Pharma");
    }

    public static void setRoot(String fxml, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/" + fxml));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1100, 720);
            
            // Apply main stylesheet
            scene.getStylesheets().add(App.class.getResource("/css/style.css").toExternalForm());
            
            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading FXML: " + fxml);
        }
    }
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}
