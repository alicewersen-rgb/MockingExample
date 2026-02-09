package com.example.payment;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentTest {

    @Test
    void shouldCreatePaymentSuccessfully() {
        Payment payment = new Payment(100, "USD", "order1");

        assertThat(payment.getAmount()).isEqualTo(100);
        assertThat(payment.getCurrency()).isEqualTo("USD");
        assertThat(payment.getOrderId()).isEqualTo("order1");
    }

    @Test
    void shouldThrowExceptionForNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(-50, "USD", "order2");
        });
    }

    @Test
    void shouldThrowExceptionForZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(0, "EUR", "order3");
        });
    }

    @Test
    void shouldThrowExceptionForNullCurrency() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(100, null, "order4");
        });
    }

    @Test
    void shouldThrowExceptionForEmptyOrderId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(50, "USD", "");
        });
    }
}
