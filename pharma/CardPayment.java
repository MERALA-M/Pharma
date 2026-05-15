package com.pharmacy.patterns;

// Strategy Pattern Implementation
public class CardPayment implements PaymentStrategy {
    private String cardNumber;
    private String expiryDate;

    public CardPayment(String cardNumber, String expiryDate) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Processing Card Payment of " + amount + " for card " + cardNumber + " (Exp: " + expiryDate + ")");
        // Simulate network call
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return "Card";
    }
}
