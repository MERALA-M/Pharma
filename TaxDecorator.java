package com.pharmacy.patterns.decorator;

public class TaxDecorator extends InvoicePricingDecorator {

    public TaxDecorator(InvoicePricing w) {

        super(w);
    }

    @Override
    public double calculateTotal() {

        return wrapped.calculateTotal() * 1.05;
    }
}