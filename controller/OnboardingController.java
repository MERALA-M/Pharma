package com.pharmacy.controller;

import com.pharmacy.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class OnboardingController {

    @FXML
    public void handleGetStarted(ActionEvent event) {
        System.out.println("[ONBOARDING] Get Started clicked. Navigating to Login.");
        App.setRoot("fxml/Login.fxml", "💊 Pharma - Security Login");
    }
}
