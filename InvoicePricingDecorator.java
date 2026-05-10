package com.pharmacy.patterns.decorator;

public abstract class InvoicePricingDecorator implements InvoicePricing {

    protected InvoicePricing wrapped;

    public InvoicePricingDecorator(InvoicePricing w) {

        this.wrapped = w;
    }
}