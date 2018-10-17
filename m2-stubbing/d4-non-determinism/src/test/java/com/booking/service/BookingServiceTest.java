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
import java.util.stream.IntStream;

import static com.booking.service.BookingResponse.BookingResponseStatus.SUCCESS;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.stream.Collectors.toSet;
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
    public void shouldPayForSingleBooking() {
        // Given
        stubFor(post(urlPathEqualTo("/payments"))
                .withRequestBody(
                        matchingJsonPath("creditCardNumber",
                                equalTo("1234-1234-1234-1234"))
                )
                .withRequestBody(
                        matchingJsonPath("creditCardExpiry",
                                equalTo("2018-02-01"))
                )
                .withRequestBody(
                        matchingJsonPath("amount",
                                equalTo("20.55"))
                )
                .withRequestBody(
                        matchingJsonPath("paymentId")
                )
                .willReturn(
                        okJson("{" +
                                "  \"paymentResponseStatus\": \"SUCCESS\"" +
                                "}")));

        stubFor(get(urlPathEqualTo("/blacklisted-cards/1234-1234-1234-1234"))
                .willReturn(okJson("{" +
                        "  \"blacklisted\": \"false\"" +
                        "}")));

        // When
        final BookingResponse bookingResponse = bookingService.payForBooking(
                new BookingPayment(
                        "1111",
                        new BigDecimal("20.55"),
                        new CreditCard("1234-1234-1234-1234",
                                LocalDate.of(2018, 2, 1))));

        // Then
        assertThat(bookingResponse.getBookingId()).isEqualTo("1111");
        assertThat(bookingResponse.getBookingResponseStatus()).isEqualTo(SUCCESS);
    }

    @Test
    public void shouldPayFor100Bookings() {
        // Given
        stubFor(post(urlPathEqualTo("/payments"))
                .withRequestBody(
                        matchingJsonPath("creditCardNumber")
                )
                .withRequestBody(
                        matchingJsonPath("creditCardExpiry")
                )
                .withRequestBody(
                        matchingJsonPath("amount")
                )
                .withRequestBody(
                        matchingJsonPath("paymentId")
                )
                .willReturn(
                        okJson("{" +
                                "  \"paymentResponseStatus\": \"SUCCESS\"" +
                                "}")));

        stubFor(get(urlPathMatching("/blacklisted-cards/.*"))
                .willReturn(okJson("{" +
                        "  \"blacklisted\": \"false\"" +
                        "}")));

        final Set<BookingPayment> batch = IntStream.range(0, 100)
                .mapToObj(this::generateBookingPayment)
                .collect(toSet());

        // When
        final Set<BookingResponse> bookingResponses = bookingService.
                payForMultipleBookingsInBatch(batch);

        // Then
        assertThat(bookingResponses).hasSize(100);
        for (int i = 0; i < 100; i++) {
            assertThat(bookingResponses)
                    .contains(new BookingResponse(Integer.toString(i), SUCCESS));
        }
    }

    private BookingPayment generateBookingPayment(int i) {
        final CreditCard creditCard = new CreditCard(
                randomCreditCardNumber(),
                LocalDate.of(i, 1, 1)
        );
        return new BookingPayment(Integer.toString(i), new BigDecimal(i), creditCard);
    }

    private String randomCreditCardNumber() {
        return randomCreditCardDigitSection() +
                "-" +
                randomCreditCardDigitSection() +
                "-" +
                randomCreditCardDigitSection() +
                "-" +
                randomCreditCardDigitSection();
    }

    private String randomCreditCardDigitSection() {
        return IntStream.range(0, 4).mapToObj(i -> Integer.toString(random.nextInt(9) + 1)).reduce((l, r) -> l + r).get();
    }
}
