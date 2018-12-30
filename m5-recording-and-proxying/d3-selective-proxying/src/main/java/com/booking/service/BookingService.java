package com.booking.service;

import com.booking.domain.BookingPayment;
import com.booking.domain.CreditCard;
import com.booking.gateway.PayBuddyFraudCheckResponse;
import com.booking.gateway.PayBuddyGateway;
import com.booking.gateway.PayBuddyPaymentResponse;

import static com.booking.gateway.PayBuddyPaymentResponse.PaymentResponseStatus.SUCCESS;
import static com.booking.service.BookingResponse.BookingResponseStatus.SUSPECTED_FRAUD;

public class BookingService {

    private final PayBuddyGateway payBuddyGateway;

    public BookingService(PayBuddyGateway payBuddyGateway) {
        this.payBuddyGateway = payBuddyGateway;
    }

    public BookingResponse payForBooking(final BookingPayment bookingPayment) {

        final CreditCard creditCard = bookingPayment.getCreditCard();

        final PayBuddyFraudCheckResponse payBuddyFraudCheckResponse =
                payBuddyGateway.fraudCheck(bookingPayment.getCreditCard().getNumber());

        if (payBuddyFraudCheckResponse.isBlacklisted()) {
            return new BookingResponse(
                    bookingPayment.getBookingId(),
                    SUSPECTED_FRAUD);
        }

        final PayBuddyPaymentResponse payBuddyPaymentResponse =
                payBuddyGateway.makePayment(creditCard.getNumber(),
                        creditCard.getExpiry(), bookingPayment.getAmount());

        if (payBuddyPaymentResponse.getPaymentResponseStatus() == SUCCESS) {
            return new BookingResponse(bookingPayment.getBookingId(),
                    BookingResponse.BookingResponseStatus.SUCCESS);
        } else {
            return new BookingResponse(bookingPayment.getBookingId(),
                    BookingResponse.BookingResponseStatus.REJECTED);
        }
    }
}
