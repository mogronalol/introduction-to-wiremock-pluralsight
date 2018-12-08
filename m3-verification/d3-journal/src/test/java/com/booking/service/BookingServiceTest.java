package com.booking.service;

import com.booking.domain.BookingPayment;
import com.booking.domain.CreditCard;
import com.booking.gateway.PayBuddyGateway;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class BookingServiceTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private BookingService bookingService;

    @Before
    public void setUp() {

        final String baseUrl = String.format("http://localhost:%s", wireMockRule.port());

        bookingService = new BookingService(new PayBuddyGateway(baseUrl));
    }

    @Test
    public void shouldPayForBookingSuccessfully() {
        // Given
        stubFor(any(anyUrl()).willReturn(ok()));

        bookingService.payForBooking(
                new BookingPayment(
                        "1111",
                        new BigDecimal("11.11"),
                        new CreditCard("1111-1111-1111-1111",
                                LocalDate.of(1111, 1, 1))));

        bookingService.payForBooking(
                new BookingPayment(
                        "2222",
                        new BigDecimal("22.22"),
                        new CreditCard("2222-2222-2222-2222",
                                LocalDate.of(2222, 2, 2))));

        final List<ServeEvent> allServeEvents = WireMock.getAllServeEvents();
    }
}
