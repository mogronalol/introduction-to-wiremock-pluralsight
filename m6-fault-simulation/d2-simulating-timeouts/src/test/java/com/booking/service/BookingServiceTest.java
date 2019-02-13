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


    @Before
    public void setUp() {
        bookingService = new BookingService(
                new PayBuddyGateway("localhost", 8080),
                16,
                50);
    }

    @Test
    public void shouldNotRejectAnyRequestsWhenUnderHeavyLoad() {
        // Given
        stubFor(get(
                urlPathEqualTo("/vat"))
                .withQueryParam("amount", equalTo("100"))
                .willReturn(
                        okJson("{\"amount\" : 20}")));

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
        assertThat(rejected).withFailMessage(
                "Expected no rejections, but got %s", rejected)
                .isZero();
    }

    @Test
    public void shouldTimeoutQuicklyIfTaxCheckIsTooSlow() {
        // Given
        stubFor(get(
                urlPathEqualTo("/vat"))
                .withQueryParam("amount", equalTo("100"))
                .willReturn(
                        okJson("{\"amount\" : 20}")));

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
        assertThat(rejected).withFailMessage(
                "Expected no rejections, but got %s", rejected)
                .isZero();
    }
}
