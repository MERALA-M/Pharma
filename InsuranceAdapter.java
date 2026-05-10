package com.pharmacy.patterns;

public class InsuranceAdapter implements PaymentStrategy {

    private ExternalInsuranceAPI insuranceAPI;

    @Override
    public boolean pay(double amount) {

        return insuranceAPI.processClaim("POLICY-ID", amount);
    }
}