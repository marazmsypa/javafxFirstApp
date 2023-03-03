package com.example.test;

import com.example.test.data.Request;
import com.example.test.data.model.Employees;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EventObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthController {
    public ObjectMapper mapper = new ObjectMapper();
    @FXML
    private Label welcomeText;
    private Employees currentEmployee;
    @FXML
    private TextField code;

    @FXML
    public void switchToMainScene(EventObject event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("main-window.fxml"));
        Parent root = loader.load();
        MainWindowController mainWindowController = loader.getController();
        mainWindowController.setDisplayCode(this.currentEmployee.getName());
        mainWindowController.setupPage();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 800);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onAuthButtonClick(ActionEvent event) throws IOException {
        login(event);
    }


    public void onEnterKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            login(keyEvent);
        }
    }

    private void login(EventObject event) throws IOException {
        Pattern pattern = Pattern.compile("^[0-9].{1,7}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(code.getText());
        boolean matchFound = matcher.find();
        if (matchFound) {
            String requestResult =
                    Request.create("http://localhost:8080/employees", "GET")
                    .addParam("code", code.getText())
                    .addHeader("Content-Type", "application/json")
                    .execute();
            if (!requestResult.equals("null")) {
                this.currentEmployee = mapper.readValue(requestResult, Employees.class);
                welcomeText.setText(this.currentEmployee.getName());
                switchToMainScene(event);
            } else {
                welcomeText.setText("Такого пользователя не существует в базе");
            }
        } else {
            welcomeText.setText("Вы неправильно ввели код");
        }
    }
}