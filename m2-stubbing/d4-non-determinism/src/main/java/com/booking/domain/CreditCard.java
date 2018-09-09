package com.booking.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreditCard {
    private final String number;
    private final LocalDate expiry;
    private final BigDecimal amount;

    public CreditCard(String number, LocalDate expiry, BigDecimal amount) {
        this.number = number;
        this.expiry = expiry;
        this.amount = amount;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
