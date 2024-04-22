package com.example.test1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class app extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader =new FXMLLoader(getClass().getResource("/Fxml/Event.fxml"));
        Scene scene =new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    public static void main (String[]args){
        launch();
    }
}