package com.flights.gateway;

public class PayBuddyPaymentResponse {
    private final String paymentId;
    private final PaymentResponseStatus paymentResponseStatus;

    public PayBuddyPaymentResponse(String paymentId, PaymentResponseStatus paymentResponseStatus) {
        this.paymentId = paymentId;
        this.paymentResponseStatus = paymentResponseStatus;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public PaymentResponseStatus getPaymentResponseStatus() {
        return paymentResponseStatus;
    }

    public enum PaymentResponseStatus {
        SUCCESS
    }
}
