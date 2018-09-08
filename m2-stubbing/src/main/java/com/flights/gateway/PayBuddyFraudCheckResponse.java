package com.flights.gateway;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PayBuddyFraudCheckResponse {
    private final boolean blacklisted;

    @JsonCreator
    public PayBuddyFraudCheckResponse(@JsonProperty("blacklisted") final boolean blacklisted) {
        this.blacklisted = blacklisted;
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }
}
