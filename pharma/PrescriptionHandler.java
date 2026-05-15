package com.pharmacy.patterns;

import javafx.scene.control.Alert;

public abstract class PrescriptionHandler {
    protected PrescriptionHandler nextHandler;

    public void setNextHandler(PrescriptionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void handleRequest(String prescriptionData);

    protected void passToNext(String prescriptionData) {
        if (nextHandler != null) {
            nextHandler.handleRequest(prescriptionData);
        } else {
            showAlert("Error", "Prescription could not be verified by any authority.");
        }
    }

    protected void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
