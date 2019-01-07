package com.booking.service;

import com.booking.gateway.PayBuddyGateway;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.RejectedExecutionException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BookingServiceTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private BookingService bookingService;

    // Four threads no delay = pass
    // Four threads 50 delay = fail
    // 16 threads 50 delay = pass
    // 16 threads 1 - 150 delay = fail


    @Before
    public void setUp() {
        bookingService = new BookingService(new PayBuddyGateway("localhost", 8080), 16, 50);
    }

    @Test
    public void shouldNotRejectAnyRequestsWhenUnderHeavyLoad() {
        // Given
        stubFor(get(
                urlPathEqualTo("/vat"))
                .withQueryParam("amount", equalTo("100"))
                .willReturn(okJson("{\"amount\" : 20}")
                        .withFixedDelay(50)));

        // When
        int rejected = 0;

        for (int i = 0; i < 1000; i++) {
            try {
                bookingService.generateInvoice("1234");
                Thread.sleep(10);
            } catch (RejectedExecutionException | InterruptedException e) {
                rejected++;
            }
        }

        // Then
        assertThat(rejected).withFailMessage("Expected no rejections, but got %s", rejected).isZero();
    }

    @Test
    public void shouldTimeoutTheTaxCheckIfRespondingSlowly() {
        // Given
        stubFor(get(
                urlPathEqualTo("/vat"))
                .withQueryParam("amount", equalTo("100"))
                .willReturn(okJson("{\"amount\" : 20}")
                        .withUniformRandomDelay(10, 1000)));

        // When
        int rejected = 0;

        for (int i = 0; i < 1000; i++) {
            try {
                bookingService.generateInvoice("1234");
                Thread.sleep(10);
            } catch (RejectedExecutionException | InterruptedException e) {
                rejected++;
            }
        }

        // Then
        assertThat(rejected).withFailMessage("Expected no rejections, but got %s", rejected).isZero();
    }
}
