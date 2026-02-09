package com.example.payment;

import com.example.NotificationException;
import com.example.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PaymentProcessorTest {

    private PaymentGateway paymentGateway;
    private NotificationService notificationService;
    private PaymentProcessor paymentProcessor;

    @BeforeEach
    void setUp() {
        paymentGateway = mock(PaymentGateway.class);
        notificationService = mock(NotificationService.class);
        paymentProcessor = new PaymentProcessor(paymentGateway, notificationService);
    }

    @Test
    void shouldProcessPaymentSuccessfully() throws PaymentException, NotificationException {
        Payment payment = new Payment(100, "USD", "order1");
        when(paymentGateway.processPayment(payment)).thenReturn(true);

        boolean result = paymentProcessor.process(payment);

        assertThat(result).isTrue();
        verify(paymentGateway).processPayment(payment);
        verify(notificationService).sendPaymentConfirmation(payment);
    }

    @Test
    void shouldReturnFalseWhenPaymentFails() throws PaymentException, NotificationException {
        Payment payment = new Payment(100, "USD", "order2");
        when(paymentGateway.processPayment(payment)).thenReturn(false);

        boolean result = paymentProcessor.process(payment);

        assertThat(result).isFalse();
        verify(paymentGateway).processPayment(payment);
        verify(notificationService, never()).sendPaymentConfirmation(any());
    }

    @Test
    void shouldThrowRuntimeExceptionWhenNotificationFails() throws PaymentException, NotificationException {
        Payment payment = new Payment(100, "USD", "order3");
        when(paymentGateway.processPayment(payment)).thenReturn(true);
        doThrow(new NotificationException("Failed")).when(notificationService).sendPaymentConfirmation(payment);

        try {
            paymentProcessor.process(payment);
        } catch (RuntimeException e) {
            assertThat(e.getCause()).isInstanceOf(NotificationException.class);
        }

        verify(paymentGateway).processPayment(payment);
        verify(notificationService).sendPaymentConfirmation(payment);
    }
}
