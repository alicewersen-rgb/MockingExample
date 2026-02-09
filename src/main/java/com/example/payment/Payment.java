package com.example.payment;

public class Payment {

    private final int amount;
    private final String currency;
    private final String orderId;

    public Payment(int amount, String currency, String orderId) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }

        if (orderId == null || orderId.isEmpty()) {
            throw new IllegalArgumentException("OrderId cannot be empty");
        }

        this.amount = amount;
        this.currency = currency;
        this.orderId = orderId;
    }

    public int getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getOrderId() {
        return orderId;
    }
}
