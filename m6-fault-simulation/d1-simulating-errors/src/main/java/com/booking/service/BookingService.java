package com.booking.service;

import com.booking.gateway.PayBuddyGateway;
import com.booking.gateway.VATCalculation;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public class BookingService {

    private final PayBuddyGateway payBuddyGateway;

    public BookingService(PayBuddyGateway payBuddyGateway) {
        this.payBuddyGateway = payBuddyGateway;
    }

    public Invoice generateInvoice(final String pendingBookingId) {

        final BigDecimal costOfBooking = getCostOfBooking(pendingBookingId);

        try {
            final VATCalculation taxCalculation = payBuddyGateway.calculateVAT(costOfBooking);
            return new Invoice(costOfBooking, taxCalculation.getAmount());
        } catch (RestClientResponseException | ResourceAccessException e) {
            e.printStackTrace();
            return new Invoice(costOfBooking, ZERO);
        }
    }

    private BigDecimal getCostOfBooking(String pendingBookingId) {
        // Normally this would go to a database and return how much a flight costs
        return new BigDecimal(100);
    }
}
