package com.booking.service;

import com.booking.domain.BookingPayment;
import com.booking.domain.CreditCard;
import com.booking.gateway.PayBuddyFraudCheckResponse;
import com.booking.gateway.PayBuddyGateway;
import com.booking.gateway.PayBuddyPaymentResponse;

import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

public class BookingService {

    private final PayBuddyGateway payBuddyGateway;

    public BookingService(PayBuddyGateway payBuddyGateway) {
        this.payBuddyGateway = payBuddyGateway;
    }

    public BookingResponse payForBooking(final BookingPayment bookingPayment) {

        final CreditCard creditCard = bookingPayment.getCreditCard();

        final PayBuddyFraudCheckResponse payBuddyFraudCheckResponse = payBuddyGateway.fraudCheck(creditCard.getNumber());

        if (payBuddyFraudCheckResponse.isBlacklisted()) {
            return new BookingResponse(bookingPayment.getBookingId(), BookingResponse.BookingResponseStatus.SUSPECTED_FRAUD);
        }

        final String paymentId = UUID.randomUUID().toString();

        final PayBuddyPaymentResponse payBuddyPaymentResponse = payBuddyGateway.makePayment(paymentId, creditCard.getNumber(), creditCard.getExpiry(), bookingPayment.getAmount());

        if (payBuddyPaymentResponse.getPaymentResponseStatus() == PayBuddyPaymentResponse.PaymentResponseStatus.SUCCESS) {
            return new BookingResponse(bookingPayment.getBookingId(), BookingResponse.BookingResponseStatus.SUCCESS);
        } else {
            return new BookingResponse(bookingPayment.getBookingId(), BookingResponse.BookingResponseStatus.REJECTED);
        }
    }

    public Set<BookingResponse> payForMultipleBookingsInBatch(final Set<BookingPayment> bookingPayment) {
        return bookingPayment.stream().map(this::payForBooking).collect(toSet());
    }
}
