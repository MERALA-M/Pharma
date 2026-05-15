package com.pharmacy.patterns.decorator;

// Decorator Pattern - Concrete Decorator
public class DeliveryFeeDecorator extends InvoicePricingDecorator {
    private double deliveryFee;

    public DeliveryFeeDecorator(InvoicePricing wrappee, double deliveryFee) {
        super(wrappee);
        this.deliveryFee = deliveryFee;
    }

    @Override
    public double calculateTotal() {
        return wrappee.calculateTotal() + deliveryFee;
    }

    @Override
    public String getPricingDetails() {
        return wrappee.getPricingDetails() + "\n+ Delivery Fee: $" + String.format("%.2f", deliveryFee);
    }
}
