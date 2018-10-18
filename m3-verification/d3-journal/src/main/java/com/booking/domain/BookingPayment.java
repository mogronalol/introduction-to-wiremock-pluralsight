package com.booking.domain;

import java.math.BigDecimal;

public class BookingPayment {
    private final String bookingId;
    private final BigDecimal amount;
    private final CreditCard creditCard;

    public BookingPayment(String bookingId, BigDecimal amount, CreditCard creditCard) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.creditCard = creditCard;
    }

    public String getBookingId() {
        return bookingId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }
}
