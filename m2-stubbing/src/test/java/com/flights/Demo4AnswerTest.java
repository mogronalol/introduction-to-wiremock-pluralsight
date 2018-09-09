package com.flights;

import com.flights.domain.CreditCard;
import com.flights.gateway.PayBuddyGateway;
import com.flights.service.BookingResponse;
import com.flights.service.BookingService;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.flights.service.BookingResponse.BookingResponseStatus.SUCCESS;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class Demo4AnswerTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private BookingService bookingService;

    @Before
    public void setUp() {
        bookingService = new BookingService(new PayBuddyGateway("localhost", wireMockRule.port()));
    }

    @Test
    public void shouldPayForBookingWithOurOwnPaymentId() {
        // Given
        stubFor(post(urlPathEqualTo("/payments"))
                .withRequestBody(matchingJsonPath("$.creditCardNumber", equalTo("1234-1234-1234-1234")))
                .withRequestBody(matchingJsonPath("$.creditCardExpiry", equalTo("2018-02-01")))
                .withRequestBody(matchingJsonPath("$.amount", equalTo("20.55")))
                .withRequestBody(matchingJsonPath("$.paymentId"))
                .willReturn(
                        okJson("{" +
                                "  \"paymentResponseStatus\": \"SUCCESS\"" +
                                "}")));

        stubFor(get(urlPathEqualTo("/blacklisted-cards/1234-1234-1234-1234")).willReturn(okJson("{" +
                "  \"blacklisted\": \"false\"" +
                "}")));

        // When
        final BookingResponse bookingResponse = bookingService.payForBookingWithFraudCheckAndGenerateOurOwnPaymentId(
                "1111",
                new CreditCard("1234-1234-1234-1234", LocalDate.of(2018, 2, 1), new BigDecimal("20.55")));

        // Then
        assertThat(bookingResponse.getBookingId()).isEqualTo("1111");
        assertThat(bookingResponse.getBookingResponseStatus()).isEqualTo(SUCCESS);
    }

    @Test
    public void shouldPayForMultipleBookingsWithOurOwnPaymentId() {
        // Given
        stubFor(post(urlPathEqualTo("/payments"))
                .withRequestBody(matchingJsonPath("$.creditCardNumber"))
                .withRequestBody(matchingJsonPath("$.creditCardExpiry"))
                .withRequestBody(matchingJsonPath("$.amount"))
                .withRequestBody(matchingJsonPath("$.paymentId"))
                .willReturn(
                        okJson("{" +
                                "  \"paymentResponseStatus\": \"SUCCESS\"" +
                                "}")));

        stubFor(get(urlPathMatching("/blacklisted-cards/.*")).willReturn(okJson("{" +
                "  \"blacklisted\": \"false\"" +
                "}")));

        // When
        final BookingResponse bookingResponse = bookingService.payForMultipleBookingWithFraudCheckAndGenerateOurOwnPaymentId(
                "1111",
                new CreditCard("1111-1234-1234-1234", LocalDate.of(2018, 2, 1), new BigDecimal("20.55")),
                new CreditCard("2222-1234-1234-1234", LocalDate.of(2018, 2, 1), new BigDecimal("20.55")),
                new CreditCard("3333-1234-1234-1234", LocalDate.of(2018, 2, 1), new BigDecimal("20.55")),
                new CreditCard("4444-1234-1234-1234", LocalDate.of(2018, 2, 1), new BigDecimal("20.55")),
                new CreditCard("5555-1234-1234-1234", LocalDate.of(2018, 2, 1), new BigDecimal("20.55")),
                new CreditCard("6666-1234-1234-1234", LocalDate.of(2018, 2, 1), new BigDecimal("20.55"))
        );

        // Then
        assertThat(bookingResponse.getBookingId()).isEqualTo("1111");
        assertThat(bookingResponse.getBookingResponseStatus()).isEqualTo(SUCCESS);
    }

}
