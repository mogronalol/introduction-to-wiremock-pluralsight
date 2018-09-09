package com.booking.gateway;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PayBuddyPaymentResponse {
    private final PaymentResponseStatus paymentResponseStatus;

    @JsonCreator
    public PayBuddyPaymentResponse(@JsonProperty("paymentResponseStatus") PaymentResponseStatus paymentResponseStatus) {

        this.paymentResponseStatus = paymentResponseStatus;
    }

    public PaymentResponseStatus getPaymentResponseStatus() {
        return paymentResponseStatus;
    }

    public enum PaymentResponseStatus {
        SUCCESS, FAILED
    }
}
