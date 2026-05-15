package com.pharmacy.patterns;

// Strategy Pattern Implementation
public class CashPayment implements PaymentStrategy {
    
    @Override
    public boolean pay(double amount) {
        if (amount > 0) {
            System.out.println("Paid $" + String.format("%.2f", amount) + " in Cash.");
            return true;
        }
        return false;
    }

    @Override
    public String getPaymentMethodName() {
        return "Cash";
    }
}
