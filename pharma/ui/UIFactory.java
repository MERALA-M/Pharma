package com.pharmacy.patterns.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

// Abstract Factory Pattern
public interface UIFactory {
    Button createPrimaryButton(String text);
    Button createSecondaryButton(String text);
    Button createDangerButton(String text);
    javafx.scene.control.Label createLabel(String text, String styleClass);
    Pane createCard();
}
