package com.pharmacy.controller;

import com.pharmacy.App;
import com.pharmacy.model.User;
import com.pharmacy.patterns.UserFactory;
import com.pharmacy.service.DataStore;
import com.pharmacy.service.SessionManager;
import com.pharmacy.model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML private StackPane rootPane;

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtFullName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Light theme animation (soft teal/cyan bubbles)
        com.pharmacy.util.AnimationHelper.applyLightLuxuryBackground(rootPane, 15);
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String fullName = txtFullName.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();

        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        DataStore db = DataStore.getInstance();
        if (db.getUsers().containsKey(username)) {
            showError("Username already exists!");
            return;
        }

        // Use Factory Pattern to create user
        String customerId = "CUST-" + System.currentTimeMillis();
        User newUser = UserFactory.createUser("CUSTOMER", customerId, username, password);
        db.getUsers().put(username, newUser);

        // Also add to Customer DB
        Customer newCustomer = new Customer(customerId, fullName, phone, email);
        db.getCustomers().add(newCustomer);

        // Auto-login
        SessionManager.getInstance().setCurrentUser(newUser);
        App.setRoot("fxml/MainLayout.fxml", "Pharmacy System - Dashboard");
    }

    @FXML
    public void handleBackToLogin(ActionEvent event) {
        App.setRoot("fxml/Login.fxml", "PharmaERP - Secure Login");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}
