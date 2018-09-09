package com.booking.service;

import com.booking.domain.BookingPayment;
import com.booking.domain.CreditCard;
import com.booking.gateway.PayBuddyFraudCheckResponse;
import com.booking.gateway.PayBuddyGateway;
import com.booking.gateway.PayBuddyPaymentResponse;

public class BookingService {

    private final PayBuddyGateway payBuddyGateway;

    public BookingService(PayBuddyGateway payBuddyGateway) {
        this.payBuddyGateway = payBuddyGateway;
    }

    public BookingResponse payForBooking(final BookingPayment bookingPayment) {

        final CreditCard creditCard = bookingPayment.getCreditCard();

        final PayBuddyFraudCheckResponse payBuddyFraudCheckResponse = payBuddyGateway.fraudCheck(creditCard.getNumber());

        if (payBuddyFraudCheckResponse.isBlacklisted()) {
            throw new RuntimeException("Cannot make payment due to blacklist");
        }

        final PayBuddyPaymentResponse payBuddyPaymentResponse = payBuddyGateway.makePayment(creditCard.getNumber(), creditCard.getExpiry(), bookingPayment.getAmount());

        if (payBuddyPaymentResponse.getPaymentResponseStatus() == PayBuddyPaymentResponse.PaymentResponseStatus.SUCCESS) {
            return new BookingResponse(bookingPayment.getBookingId(), payBuddyPaymentResponse.getPaymentId(), BookingResponse.BookingResponseStatus.SUCCESS);
        }

        throw new RuntimeException("Unsupported response status: " + payBuddyPaymentResponse.getPaymentResponseStatus());
    }
}
