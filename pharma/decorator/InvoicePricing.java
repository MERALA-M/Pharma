package com.pharmacy.patterns.decorator;

// Decorator Pattern - Component
public interface InvoicePricing {
    double calculateTotal();
    String getPricingDetails();
}
