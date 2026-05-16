package com.pharmacy.controller;

import com.pharmacy.App;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private VBox mainContainer;
    @FXML private StackPane logoContainer;
    @FXML private javafx.scene.Group heartLogo;
    @FXML private VBox brandTextContainer;
    @FXML private VBox actionContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Light & Fresh Luxury Background Animation
        com.pharmacy.util.AnimationHelper.applyLightLuxuryBackground(rootPane, 15);

        // Initial state: hidden
        logoContainer.setOpacity(0);
        logoContainer.setScaleX(0.5);
        logoContainer.setScaleY(0.5);
        brandTextContainer.setOpacity(0);
        brandTextContainer.setTranslateY(20);
        actionContainer.setOpacity(0);

        // Run animations
        runEntranceAnimations();
    }

    private void runEntranceAnimations() {
        // 1. Logo Entrance (Fade + Scale) - Slower and more majestic
        FadeTransition logoFade = new FadeTransition(Duration.seconds(2.0), logoContainer);
        logoFade.setToValue(1);

        ScaleTransition logoScale = new ScaleTransition(Duration.seconds(2.0), logoContainer);
        logoScale.setToX(1);
        logoScale.setToY(1);
        logoScale.setInterpolator(Interpolator.EASE_BOTH);

        // 2. Heartbeat Pulsing Animation (Subtle and Premium)
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(1.2), heartLogo);
        pulse.setFromX(2.0);
        pulse.setFromY(2.0);
        pulse.setToX(2.1);
        pulse.setToY(2.1);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.setInterpolator(Interpolator.EASE_BOTH);

        // 3. Text Entrance
        FadeTransition textFade = new FadeTransition(Duration.seconds(1), brandTextContainer);
        textFade.setDelay(Duration.seconds(1));
        textFade.setToValue(1);

        TranslateTransition textMove = new TranslateTransition(Duration.seconds(1), brandTextContainer);
        textMove.setDelay(Duration.seconds(1));
        textMove.setToY(0);

        // 4. Button Entrance
        FadeTransition actionFade = new FadeTransition(Duration.seconds(1), actionContainer);
        actionFade.setDelay(Duration.seconds(1.8));
        actionFade.setToValue(1);

        // Play everything
        ParallelTransition entrance = new ParallelTransition(logoFade, logoScale, textFade, textMove, actionFade);
        entrance.play();
        pulse.play();
    }

    @FXML
    public void handleGetStarted(ActionEvent event) {
        System.out.println("[SPLASH] Navigating to Login.");
        App.setRoot("fxml/Login.fxml", "💊 Pharma - Secure Login");
    }
}
