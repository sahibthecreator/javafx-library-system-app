package com.assignment.javaendassignment;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginScreenController {

    private Database db;
    @FXML
    private Text errorMsg;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginBtn;

    MainScreenController controller;


    @FXML
    protected void login() {
        db.getUsers().forEach(user -> {
            if (usernameField.getText().equals(user.getUsername())) {
                if (passwordField.getText().equals(user.getPassword())) {
                    openMainWindow(user);
                } else {
                    errorMsg.setText("Failed! Incorrect password");
                }
            } else {
                errorMsg.setText("Failed! Incorrect username");
            }
        });
    }

    private void openMainWindow(User user) {
        Stage stage = (Stage) loginBtn.getScene().getWindow();
        stage.close();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/MainScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 450);
            controller = fxmlLoader.getController();
            controller.setDb(db);
            controller.setWelcomeText(user);
            scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
            stage.setTitle("Main");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println(String.format("Error: %s", e.getMessage()));
        }
    }

    public void setDb(Database database) {
        db = database;
    }

    public void onClose() {
        if (controller != null) {
            controller.saveOnClose();
        }
    }
}