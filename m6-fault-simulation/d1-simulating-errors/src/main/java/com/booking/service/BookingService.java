package com.booking.service;

import com.booking.gateway.PayBuddyGateway;
import com.booking.gateway.VATCalculation;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.math.BigDecimal.ZERO;

public class BookingService {

    private final PayBuddyGateway payBuddyGateway;
    private ExecutorService executorService;

    public BookingService(PayBuddyGateway payBuddyGateway) {
        this.payBuddyGateway = payBuddyGateway;
        this.executorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(1000),
                new ThreadPoolExecutor.CallerRunsPolicy());
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
