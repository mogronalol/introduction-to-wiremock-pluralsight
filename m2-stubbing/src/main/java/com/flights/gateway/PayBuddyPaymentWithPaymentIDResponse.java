package com.flights.gateway;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PayBuddyPaymentWithPaymentIDResponse {
    private final PayBuddyPaymentResponse.PaymentResponseStatus paymentResponseStatus;

    @JsonCreator
    public PayBuddyPaymentWithPaymentIDResponse(@JsonProperty("paymentResponseStatus") PayBuddyPaymentResponse.PaymentResponseStatus paymentResponseStatus) {

        this.paymentResponseStatus = paymentResponseStatus;
    }

    public PayBuddyPaymentResponse.PaymentResponseStatus getPaymentResponseStatus() {
        return paymentResponseStatus;
    }
}
