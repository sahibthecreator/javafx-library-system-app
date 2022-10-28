package com.assignment.javaendassignment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public Database db;
    LoginScreenController controller;
    @Override
    public void start(Stage stage) throws IOException {
        db = new Database();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/LoginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 350);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        stage.setTitle("Login!");
        stage.setScene(scene);
        stage.show();
        controller = fxmlLoader.getController();
        controller.setDb(db);
    }
    @Override
    public void stop(){
        controller.onClose();
    }

    public static void main(String[] args) {
        launch();
    }
}
