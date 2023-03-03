package com.example.test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("main-window.fxml"));
        Parent root = loader.load();
        MainWindowController mainWindowController = loader.getController();
        mainWindowController.setupPage();
        Scene scene = new Scene(root, 800, 800);
        stage.setScene(scene);
        stage.show();
        // FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("auth-page.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 800, 800);
        //stage.setTitle("Hello!");
       // stage.setScene(scene);
        //stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}