package com.pharmacy.patterns;

// Strategy Pattern Interface
public interface PaymentStrategy {
    boolean pay(double amount);
    String getPaymentMethodName();
}
