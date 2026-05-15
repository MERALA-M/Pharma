package com.pharmacy.patterns;

// External System (Simulated)
public class ExternalInsuranceAPI {
    public boolean processClaim(String policyNumber, double amount) {
        System.out.println("Contacting Insurance API for policy: " + policyNumber);
        return true; // Approved
    }
}
