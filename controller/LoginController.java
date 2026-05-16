package com.pharmacy.controller;

import com.pharmacy.App;
import com.pharmacy.model.User;
import com.pharmacy.service.DataStore;
import com.pharmacy.service.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private StackPane rootPane;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Light theme animation (soft teal/cyan bubbles)
        com.pharmacy.util.AnimationHelper.applyLightLuxuryBackground(rootPane, 15);
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }

        Map<String, User> users = DataStore.getInstance().getUsers();
        User user = users.get(username);

        if (user != null && user.getPassword().equals(password)) {
            // Success
            SessionManager.getInstance().setCurrentUser(user);
            System.out.println("Login successful. Role: " + user.getRole());
            
            // Navigate to Main Dashboard
            App.setRoot("fxml/MainLayout.fxml", "Pharmacy System - Dashboard");
        } else {
            showError("Invalid username or password.");
        }
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        App.setRoot("fxml/Register.fxml", "Pharmacy System - Register");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}
