package com.booking.service;

import com.booking.gateway.PayBuddyGateway;
import com.booking.gateway.VATCalculation;
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigDecimal;
import java.util.concurrent.*;

import static java.math.BigDecimal.ZERO;

public class BookingService {

    private final PayBuddyGateway payBuddyGateway;
    private ExecutorService executorService;

    public BookingService(PayBuddyGateway payBuddyGateway,
                          int fixedAmountOfThreads,
                          int queueCapacity) {

        this.payBuddyGateway = payBuddyGateway;

        final BlockingQueue<Runnable> blockingQueue =
                new ArrayBlockingQueue<Runnable>(queueCapacity);

        final RejectedExecutionHandler rejectedExecutionHandler =
                new ThreadPoolExecutor.AbortPolicy();

        this.executorService =
                new ThreadPoolExecutor(
                        fixedAmountOfThreads,
                        fixedAmountOfThreads,
                0L,
                        TimeUnit.MILLISECONDS,
                        blockingQueue,
                        rejectedExecutionHandler);
    }

    public Future<Invoice> generateInvoice(final String pendingBookingId) {

        return executorService.submit(() -> {

            final BigDecimal costOfBooking = getCostOfBooking(pendingBookingId);

            try {
                final VATCalculation taxCalculation = payBuddyGateway.calculateVAT(costOfBooking);
                return new Invoice(costOfBooking, taxCalculation.getAmount());
            } catch (HttpServerErrorException e) {
                e.printStackTrace();
                return new Invoice(costOfBooking, ZERO);
            }
        });
    }

    private BigDecimal getCostOfBooking(String pendingBookingId) {
        // Normally this would go to a database and return how much a flight costs
        return new BigDecimal(100);
    }
}
