package com.example.payment;

public interface PaymentGateway {
    /**
     * Försök att behandla betalningen
     * @param payment betalningsobjekt
     * @return true om betalningen lyckades, false annars
     * @throws PaymentException om något går fel
     */
    boolean processPayment(Payment payment) throws PaymentException;

    Object charge(Payment payment);
}
