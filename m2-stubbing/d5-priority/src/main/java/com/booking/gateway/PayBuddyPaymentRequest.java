package com.booking.gateway;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PayBuddyPaymentRequest {
    private final String paymentId;
    private final String creditCardNumber;
    private final LocalDate creditCardExpiry;
    private final BigDecimal amount;

    public PayBuddyPaymentRequest(String paymentId, String creditCardNumber, LocalDate creditCardExpiry, BigDecimal amount) {
        this.paymentId = paymentId;

        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiry = creditCardExpiry;
        this.amount = amount;
    }

    public String getPaymentId() {
        return paymentId;
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
}
