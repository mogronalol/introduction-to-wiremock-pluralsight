package com.booking.gateway;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class VATCalculation {

    private final BigDecimal amount;

    @JsonCreator
    public VATCalculation(@JsonProperty("amount") BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
