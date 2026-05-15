package com.pharmacy.patterns;

public class PharmacistHandler extends PrescriptionHandler {
    @Override
    public void handleRequest(String prescriptionData) {
        if (prescriptionData.contains("Standard")) {
            showAlert("Approval", "Prescription approved by Pharmacist (Standard Check).");
        } else {
            // Pass to manager
            passToNext(prescriptionData);
        }
    }
}
