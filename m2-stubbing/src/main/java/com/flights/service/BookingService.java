package com.flights.service;

import com.flights.domain.CreditCard;
import com.flights.gateway.PayBuddyFraudCheckResponse;
import com.flights.gateway.PayBuddyGateway;
import com.flights.gateway.PayBuddyPaymentResponse;
import com.flights.gateway.PayBuddyPaymentWithPaymentIDResponse;

import java.util.UUID;
import java.util.stream.Stream;

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

    public BookingResponse payForBookingWithFraudCheck(final String bookingId,
                                                       final CreditCard creditCard) {

        final PayBuddyFraudCheckResponse payBuddyFraudCheckResponse = payBuddyGateway.fraudCheck(creditCard.getNumber());

        if (!payBuddyFraudCheckResponse.isBlacklisted()) {
            return payForBooking(bookingId, creditCard);
        }

        throw new RuntimeException("Unsupported response status: " + payBuddyFraudCheckResponse.isBlacklisted());
    }

    public BookingResponse payForBookingWithFraudCheckAndGenerateOurOwnPaymentId(final String bookingId,
                                                                                 final CreditCard creditCard) {

        final PayBuddyFraudCheckResponse payBuddyFraudCheckResponse = payBuddyGateway.fraudCheck(creditCard.getNumber());

        if (!payBuddyFraudCheckResponse.isBlacklisted()) {

            final String paymentId = UUID.randomUUID().toString();

            final PayBuddyPaymentWithPaymentIDResponse response = payBuddyGateway.makePaymentWithId(paymentId, creditCard.getNumber(), creditCard.getExpiry(), creditCard.getAmount());

            if (response.getPaymentResponseStatus() == PayBuddyPaymentResponse.PaymentResponseStatus.SUCCESS) {
                return new BookingResponse(bookingId, paymentId, BookingResponse.BookingResponseStatus.SUCCESS);
            }

            throw new RuntimeException("Unsupported response status: " + response.getPaymentResponseStatus());
        }

        throw new RuntimeException("Unsupported response status: " + payBuddyFraudCheckResponse.isBlacklisted());
    }

    public BookingResponse payForMultipleBookingWithFraudCheckAndGenerateOurOwnPaymentId(final String bookingId,
                                                                                         final CreditCard... creditCards) {

        final String paymentId = UUID.randomUUID().toString();

        Stream.of(creditCards).forEach(creditCard -> {
            final PayBuddyFraudCheckResponse payBuddyFraudCheckResponse = payBuddyGateway.fraudCheck(creditCard.getNumber());

            if (!payBuddyFraudCheckResponse.isBlacklisted()) {
                final PayBuddyPaymentWithPaymentIDResponse response = payBuddyGateway.makePaymentWithId(paymentId, creditCard.getNumber(), creditCard.getExpiry(), creditCard.getAmount());

                if (response.getPaymentResponseStatus() != PayBuddyPaymentResponse.PaymentResponseStatus.SUCCESS) {
                    throw new RuntimeException("Unsupported response status: " + response.getPaymentResponseStatus());
                }
            } else {
                throw new RuntimeException("Unsupported response status: " + payBuddyFraudCheckResponse.isBlacklisted());
            }
        });

        return new BookingResponse(bookingId, paymentId, BookingResponse.BookingResponseStatus.SUCCESS);
    }
}
