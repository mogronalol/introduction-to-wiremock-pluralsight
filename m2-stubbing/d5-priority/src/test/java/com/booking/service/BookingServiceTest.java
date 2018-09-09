package com.booking.service;

import com.booking.domain.BookingPayment;
import com.booking.domain.CreditCard;
import com.booking.gateway.PayBuddyGateway;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;
import java.util.Set;

import static com.booking.service.BookingResponse.BookingResponseStatus.REJECTED;
import static com.booking.service.BookingResponse.BookingResponseStatus.SUCCESS;
import static com.booking.service.BookingResponse.BookingResponseStatus.SUSPECTED_FRAUD;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BookingServiceTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private BookingService bookingService;
    private Random random = new Random();

    @Before
    public void setUp() {
        bookingService = new BookingService(new PayBuddyGateway("localhost", wireMockRule.port()));
    }

    @Test
    public void shouldPayForMultipleBookingsOneOfWhichWillFailOnTheFraudCheck() {
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

        stubFor(post(urlPathEqualTo("/payments"))
                .withRequestBody(matchingJsonPath("$.creditCardNumber", equalTo("0000-0000-0000-0000")))
                .withRequestBody(matchingJsonPath("$.creditCardExpiry", equalTo("2099-09-09")))
                .withRequestBody(matchingJsonPath("$.amount", equalTo("99.99")))
                .withRequestBody(matchingJsonPath("$.paymentId"))
                .willReturn(
                        okJson("{" +
                                "  \"paymentResponseStatus\": \"FAILED\"" +
                                "}")));

        stubFor(get(urlPathMatching("/blacklisted-cards/.*")).willReturn(okJson("{" +
                "  \"blacklisted\": \"false\"" +
                "}")));

        stubFor(get(urlPathMatching("/blacklisted-cards/9999-9999-9999-9999")).willReturn(okJson("{" +
                "  \"blacklisted\": \"true\"" +
                "}")));

        final BookingPayment toSucceed1 = new BookingPayment(
                "1",
                new BigDecimal("1.11"),
                new CreditCard("1111-1111-1111-1111", LocalDate.of(2011, 1, 1))
        );

        final BookingPayment toSucceed2 = new BookingPayment(
                "2",
                new BigDecimal("2.22"),
                new CreditCard("2222-2222-2222-2222", LocalDate.of(2022, 2, 2))
        );

        final BookingPayment toFailOnFraud = new BookingPayment(
                "9",
                new BigDecimal("9.99"),
                new CreditCard("9999-9999-9999-9999", LocalDate.of(2099, 9, 9))
        );

        final BookingPayment toFailOnPayment = new BookingPayment(
                "0",
                new BigDecimal("99.99"),
                new CreditCard("0000-0000-0000-0000", LocalDate.of(2099, 9, 9))
        );

        // When
        final Set<BookingResponse> bookingResponses = bookingService.payForMultipleBookingsInBatch(toFailOnFraud, toFailOnPayment, toSucceed1, toSucceed2);

        // Then
        assertThat(bookingResponses).containsExactlyInAnyOrder(
                new BookingResponse("1", SUCCESS),
                new BookingResponse("2", SUCCESS),
                new BookingResponse("9", SUSPECTED_FRAUD),
                new BookingResponse("0", REJECTED)
        );
    }
}
