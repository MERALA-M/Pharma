package com.pharmacy.patterns.decorator;

// Decorator Pattern - Concrete Component
public class BaseInvoicePricing implements InvoicePricing {
    private double baseTotal;

    public BaseInvoicePricing(double baseTotal) {
        this.baseTotal = baseTotal;
    }

    @Override
    public double calculateTotal() {
        return baseTotal;
    }

    @Override
    public String getPricingDetails() {
        return "Base Total: $" + String.format("%.2f", baseTotal);
    }
}
