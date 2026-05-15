package com.pharmacy.patterns;

// Adapter Pattern - Adapting ExternalInsuranceAPI to PaymentStrategy
public class InsuranceAdapter implements PaymentStrategy {
    private ExternalInsuranceAPI insuranceAPI;
    private String policyNumber;

    public InsuranceAdapter(ExternalInsuranceAPI insuranceAPI, String policyNumber) {
        this.insuranceAPI = insuranceAPI;
        this.policyNumber = policyNumber;
    }

    @Override
    public boolean pay(double amount) {
        return insuranceAPI.processClaim(policyNumber, amount);
    }

    @Override
    public String getPaymentMethodName() {
        return "Insurance";
    }
}
