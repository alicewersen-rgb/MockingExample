package com.example.payment;

public class Payment {
    private final double amount;
    private final String currency;
    private final String customerId;

    public Payment(double amount, String currency, String customerId) {
        this.amount = amount;
        this.currency = currency;
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCustomerId() {
        return customerId;
    }
}