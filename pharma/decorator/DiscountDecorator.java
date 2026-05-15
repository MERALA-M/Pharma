package com.pharmacy.patterns.decorator;

// Decorator Pattern - Concrete Decorator
public class DiscountDecorator extends InvoicePricingDecorator {
    private double discountPercentage;

    public DiscountDecorator(InvoicePricing wrappee, double discountPercentage) {
        super(wrappee);
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double calculateTotal() {
        return wrappee.calculateTotal() * (1 - discountPercentage);
    }

    @Override
    public String getPricingDetails() {
        return wrappee.getPricingDetails() + "\n- Discount (" + (discountPercentage * 100) + "%)";
    }
}
