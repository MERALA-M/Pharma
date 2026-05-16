package com.pharmacy.controller;

import com.pharmacy.patterns.ManagerHandler;
import com.pharmacy.patterns.PharmacistHandler;
import com.pharmacy.patterns.PrescriptionHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PrescriptionsController {

    @FXML private ComboBox<String> cmbPrescriptionType;
    @FXML private TextArea txtPrescriptionDetails;
    
    private PrescriptionHandler chain;

    @FXML
    public void initialize() {
        cmbPrescriptionType.getItems().addAll("Standard (e.g., Antibiotics)", "Controlled (e.g., Painkillers)", "Unknown (Requires further auth)");
        cmbPrescriptionType.getSelectionModel().selectFirst();
        
        // Setup Chain of Responsibility
        PrescriptionHandler pharmacist = new PharmacistHandler();
        PrescriptionHandler manager = new ManagerHandler();
        
        pharmacist.setNextHandler(manager);
        this.chain = pharmacist;
    }

    @FXML
    public void handleVerify(ActionEvent event) {
        String data = cmbPrescriptionType.getValue();
        if (txtPrescriptionDetails.getText() != null && !txtPrescriptionDetails.getText().isEmpty()) {
            data += " - " + txtPrescriptionDetails.getText();
        }
        // Start the chain
        chain.handleRequest(data);
    }
}
