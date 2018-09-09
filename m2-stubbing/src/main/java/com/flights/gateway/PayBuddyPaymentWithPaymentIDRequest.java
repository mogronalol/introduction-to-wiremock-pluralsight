package com.flights.gateway;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PayBuddyPaymentWithPaymentIDRequest {
    private final String creditCardNumber;
    private final LocalDate creditCardExpiry;
    private final BigDecimal amount;
    private final String paymentId;

    public PayBuddyPaymentWithPaymentIDRequest(String creditCardNumber, LocalDate creditCardExpiry, BigDecimal amount, String paymentId) {

        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiry = creditCardExpiry;
        this.amount = amount;
        this.paymentId = paymentId;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public LocalDate getCreditCardExpiry() {
        return creditCardExpiry;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getPaymentId() {
        return paymentId;
    }
}

