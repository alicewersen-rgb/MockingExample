package com.example;

import com.example.payment.Payment;

public interface NotificationService {
    void sendBookingConfirmation(Booking booking) throws NotificationException;
    void sendCancellationConfirmation(Booking booking) throws NotificationException;
    void sendPaymentConfirmation(Payment payment) throws NotificationException;

    void notify(Payment payment);
}
