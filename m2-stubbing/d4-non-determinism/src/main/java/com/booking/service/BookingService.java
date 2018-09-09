package com.booking.service;

import com.booking.domain.BookingPayment;
import com.booking.gateway.PayBuddyGateway;

public class BookingService {

    private final PayBuddyGateway payBuddyGateway;

    public BookingService(PayBuddyGateway payBuddyGateway) {
        this.payBuddyGateway = payBuddyGateway;
    }

    public BookingResponse payForBooking(final BookingPayment bookingPayment) {

//        UUID.randomUUID();
//
//        final PayBuddyPaymentResponse payBuddyPaymentResponse = payBuddyGateway.makePayment(creditCard.getNumber(), creditCard.getExpiry(), creditCard.getAmount());
//
//        if (payBuddyPaymentResponse.getPaymentResponseStatus() == PayBuddyPaymentResponse.PaymentResponseStatus.SUCCESS) {
//            return new BookingResponse(bookingId, payBuddyPaymentResponse.getPaymentId(), BookingResponse.BookingResponseStatus.SUCCESS);
//        }
//
//        throw new RuntimeException("Unsupported response status: " + payBuddyPaymentResponse.getPaymentResponseStatus());

        return null;
    }

    public BookingResponse payForBookingWithMultipleCards(final BookingPayment bookingPayment) {

//        final PayBuddyPaymentResponse payBuddyPaymentResponse = payBuddyGateway.makePayment(creditCard.getNumber(), creditCard.getExpiry(), creditCard.getAmount());
//
//        if (payBuddyPaymentResponse.getPaymentResponseStatus() == PayBuddyPaymentResponse.PaymentResponseStatus.SUCCESS) {
//            return new BookingResponse(bookingId, payBuddyPaymentResponse.getPaymentId(), BookingResponse.BookingResponseStatus.SUCCESS);
//        }
//
//        throw new RuntimeException("Unsupported response status: " + payBuddyPaymentResponse.getPaymentResponseStatus());

        return null;
    }
}
