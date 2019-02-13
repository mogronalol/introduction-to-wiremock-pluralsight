package com.booking.service;

import com.booking.gateway.PayBuddyGateway;
import com.booking.gateway.VATCalculation;

import java.math.BigDecimal;

public class BookingService {

    private final PayBuddyGateway payBuddyGateway;

    public BookingService(PayBuddyGateway payBuddyGateway) {
        this.payBuddyGateway = payBuddyGateway;
    }

    public Invoice generateInvoice(final String pendingBookingId) {

        final BigDecimal costOfBooking = getCostOfBooking(pendingBookingId);

        final VATCalculation taxCalculation = payBuddyGateway
                .calculateVAT(costOfBooking);

        return new Invoice(costOfBooking, taxCalculation.getAmount());
    }

    private BigDecimal getCostOfBooking(String pendingBookingId) {
        // Normally this would go to a database and return how much a flight costs
        return new BigDecimal(100);
    }
}
