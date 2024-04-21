package com.example.jessem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {
    @Override
    public  void start(Stage primaryStage) throws  Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Front.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 1300,700));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
