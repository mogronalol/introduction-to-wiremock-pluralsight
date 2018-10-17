package com.booking.service;

import com.booking.domain.BookingPayment;
import com.booking.domain.CreditCard;
import com.booking.gateway.PayBuddyGateway;
import com.booking.gateway.PayBuddyPaymentResponse;
import com.booking.gateway.PayBuddyPaymentResponse.PaymentResponseStatus;
import com.booking.service.BookingResponse.BookingResponseStatus;

public class BookingService {

    private final PayBuddyGateway payBuddyGateway;

    public BookingService(PayBuddyGateway payBuddyGateway) {
        this.payBuddyGateway = payBuddyGateway;
    }

    public BookingResponse payForBooking(final BookingPayment bookingPayment) {

        final CreditCard creditCard = bookingPayment.getCreditCard();

        final PayBuddyPaymentResponse payBuddyPaymentResponse =
                payBuddyGateway.makePayment(
                        creditCard.getNumber(),
                        creditCard.getExpiry(),
                        bookingPayment.getAmount());

        final PaymentResponseStatus status = payBuddyPaymentResponse.getPaymentResponseStatus();

        if (status == PaymentResponseStatus.SUCCESS) {
            return new BookingResponse(
                    bookingPayment.getBookingId(),
                    payBuddyPaymentResponse.getPaymentId(),
                    BookingResponseStatus.COMPLETE);
        } else {
            return new BookingResponse(
                    bookingPayment.getBookingId(),
                    payBuddyPaymentResponse.getPaymentId()
                    , BookingResponseStatus.REJECTED);
        }
    }
}
