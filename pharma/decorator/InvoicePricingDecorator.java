package com.pharmacy.patterns.decorator;

// Decorator Pattern - Decorator Base
public abstract class InvoicePricingDecorator implements InvoicePricing {
    protected InvoicePricing wrappee;

    public InvoicePricingDecorator(InvoicePricing wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public double calculateTotal() {
        return wrappee.calculateTotal();
    }

    @Override
    public String getPricingDetails() {
        return wrappee.getPricingDetails();
    }
}
