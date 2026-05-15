package com.pharmacy.patterns.decorator;

// Decorator Pattern - Concrete Decorator
public class TaxDecorator extends InvoicePricingDecorator {
    private double taxRate;

    public TaxDecorator(InvoicePricing wrappee, double taxRate) {
        super(wrappee);
        this.taxRate = taxRate;
    }

    @Override
    public double calculateTotal() {
        return wrappee.calculateTotal() * (1 + taxRate);
    }

    @Override
    public String getPricingDetails() {
        return wrappee.getPricingDetails() + "\n+ Tax (" + (taxRate * 100) + "%)";
    }
}
