package com.booking.service;

import com.booking.domain.CreditCard;
import com.booking.gateway.PayBuddyGateway;
import com.booking.gateway.PayBuddyPaymentResponse;

public class BookingService {

    private final PayBuddyGateway payBuddyGateway;

    public BookingService(PayBuddyGateway payBuddyGateway) {
        this.payBuddyGateway = payBuddyGateway;
    }

    public BookingResponse payForBooking(final String bookingId,
                                         final CreditCard creditCard) {

        final PayBuddyPaymentResponse payBuddyPaymentResponse = payBuddyGateway.makePayment(creditCard.getNumber(), creditCard.getExpiry(), creditCard.getAmount());

        if (payBuddyPaymentResponse.getPaymentResponseStatus() == PayBuddyPaymentResponse.PaymentResponseStatus.SUCCESS) {
            return new BookingResponse(bookingId, payBuddyPaymentResponse.getPaymentId(), BookingResponse.BookingResponseStatus.SUCCESS);
        }

        throw new RuntimeException("Unsupported response status: " + payBuddyPaymentResponse.getPaymentResponseStatus());
    }

    public BookingResponse payForBookingWithMultipleCards(final String bookingId,
                                         final CreditCard ... creditCard) {

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
