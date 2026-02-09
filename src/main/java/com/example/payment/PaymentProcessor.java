package com.example.payment;

import com.example.NotificationException;
import com.example.NotificationService;

/**
 * PaymentProcessor ansvarar för att processa betalningar via en PaymentGateway
 * och för att notifiera användaren via NotificationService.
 *
 * Refaktoreringsbeslut:
 * - PaymentGateway extraherades som beroende via konstruktor (Dependency Injection)
 *   för att kunna mocka gateway i tester.
 * - NotificationService injiceras via konstruktorn för att kunna verifiera
 *   notifieringar i tester.
 * - Exception-hantering (PaymentException och NotificationException) hanteras
 *   på ett mer testbart sätt.
 */

public class PaymentProcessor {

    private final PaymentGateway gateway;
    private final NotificationService notificationService;

    public PaymentProcessor(PaymentGateway gateway, NotificationService notificationService) {
        this.gateway = gateway;
        this.notificationService = notificationService;
    }

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
