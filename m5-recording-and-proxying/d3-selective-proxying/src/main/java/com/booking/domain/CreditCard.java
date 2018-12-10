package com.booking.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreditCard {
    private final String number;
    private final LocalDate expiry;

    public CreditCard(String number, LocalDate expiry) {
        this.number = number;
        this.expiry = expiry;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getExpiry() {
        return expiry;
    }
}
