package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        // Starting the Login Screen
        Parent root = FXMLLoader.load(getClass().getResource("/Login/LoginUI.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 492, 388));
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}