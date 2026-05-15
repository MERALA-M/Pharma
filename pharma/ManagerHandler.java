package com.pharmacy.patterns;

public class ManagerHandler extends PrescriptionHandler {
    @Override
    public void handleRequest(String prescriptionData) {
        if (prescriptionData.contains("Controlled")) {
            showAlert("Approval", "Prescription approved by Manager (Controlled Substance).");
        } else {
            passToNext(prescriptionData);
        }
    }
}
