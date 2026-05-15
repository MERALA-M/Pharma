package com.pharmacy.patterns.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

// Concrete Factory Pattern Implementation
public class LuxuryUIFactory implements UIFactory {

    @Override
    public Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("btn-primary");
        return btn;
    }

    @Override
    public Button createSecondaryButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("btn-secondary");
        return btn;
    }

    @Override
    public Button createDangerButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("btn-danger");
        return btn;
    }

    @Override
    public javafx.scene.control.Label createLabel(String text, String styleClass) {
        javafx.scene.control.Label label = new javafx.scene.control.Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    @Override
    public Pane createCard() {
        VBox card = new VBox();
        card.getStyleClass().add("card");
        return card;
    }
}
