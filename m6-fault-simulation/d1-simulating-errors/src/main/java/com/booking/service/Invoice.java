package com.booking.service;

import java.math.BigDecimal;

public class Invoice {
    private final BigDecimal costOfFlight;
    private final BigDecimal tax;

    public Invoice(BigDecimal costOfFlight, BigDecimal tax) {
        this.costOfFlight = costOfFlight;
        this.tax = tax;
    }

    public BigDecimal getCostOfFlight() {
        return costOfFlight;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getTotal() {
        return costOfFlight.add(tax);
    }
}
