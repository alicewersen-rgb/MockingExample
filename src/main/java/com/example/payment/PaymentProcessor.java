package com.example.payment;

import com.example.NotificationException;
import com.example.NotificationService;

public class PaymentProcessor {

    private final PaymentGateway gateway;
    private final NotificationService notificationService;

    public PaymentProcessor(PaymentGateway gateway, NotificationService notificationService) {
        this.gateway = gateway;
        this.notificationService = notificationService;
    }

    /**
     * Processar en betalning via gateway.
     *
     * @param payment betalning
     * @return true om betalningen lyckades
     */
    public boolean process(Payment payment) {
        try {
            boolean success = gateway.processPayment(payment);

            if (success) {
                notificationService.sendPaymentConfirmation(payment);
            }

            return success;
        } catch (PaymentException e) {
            System.out.println("Payment failed: " + e.getMessage());
            return false;
        } catch (NotificationException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean processPayment(Payment payment) {
        return false;
    }
}
