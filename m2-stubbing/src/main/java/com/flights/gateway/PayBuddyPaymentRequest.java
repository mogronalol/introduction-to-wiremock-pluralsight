package com.flights.gateway;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PayBuddyPaymentRequest {
    private final String creditCardNumber;
    private final LocalDate creditCardExpiry;
    private final BigDecimal amount;

    public PayBuddyPaymentRequest(String creditCardNumber, LocalDate creditCardExpiry, BigDecimal amount) {

        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiry = creditCardExpiry;
        this.amount = amount;
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
