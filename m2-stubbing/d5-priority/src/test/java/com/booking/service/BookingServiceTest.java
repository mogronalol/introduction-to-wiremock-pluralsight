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
import java.util.Set;

import static com.booking.service.BookingResponse.BookingResponseStatus.SUCCESS;
import static com.booking.service.BookingResponse.BookingResponseStatus.SUSPECTED_FRAUD;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BookingServiceTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private BookingService bookingService;

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

        stubFor(get(urlPathEqualTo("/blacklisted-cards/0000-0000-0000-0000"))
                .atPriority(1)
                .willReturn(okJson("{" +
                        "  \"blacklisted\": \"true\"" +
                        "}")));

        stubFor(get(urlPathMatching("/blacklisted-cards/.*"))
                .atPriority(100)
                .willReturn(okJson("{" +
                        "  \"blacklisted\": \"false\"" +
                        "}")));

        final BookingPayment fraud = new BookingPayment(
                "0",
                new BigDecimal("1.11"),
                new CreditCard("0000-0000-0000-0000",
                        LocalDate.of(2011, 1, 1))
        );

        final BookingPayment toSucceed1 = new BookingPayment(
                "1",
                new BigDecimal("1.11"),
                new CreditCard("1111-1111-1111-1111",
                        LocalDate.of(2011, 1, 1))
        );

        final BookingPayment toSucceed2 = new BookingPayment(
                "2",
                new BigDecimal("2.22"),
                new CreditCard("2222-2222-2222-2222",
                        LocalDate.of(2022, 2, 2))
        );

        // When
        final Set<BookingResponse> bookingResponses = bookingService
                .payForMultipleBookingsInBatch(toSucceed1, toSucceed2, fraud);

        // Then
        assertThat(bookingResponses).containsExactlyInAnyOrder(
                new BookingResponse("1", SUCCESS),
                new BookingResponse("2", SUCCESS),
                new BookingResponse("0", SUSPECTED_FRAUD)
        );
    }
}
