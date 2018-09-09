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

public class Demo2AnswerTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private BookingService bookingService;

    @Before
    public void setUp() {
        bookingService = new BookingService(new PayBuddyGateway("localhost", wireMockRule.port()));
    }

    @Test
    public void shouldPayForBookingWithFraudCheck() {
        // Given
        stubFor(post(urlPathEqualTo("/payments")).withRequestBody(
                equalToJson("{" +
                        "  \"creditCardNumber\": \"1234-1234-1234-1234\"," +
                        "  \"creditCardExpiry\": \"2018-02-01\"," +
                        "  \"amount\": 20.55" +
                        "}"))
                .willReturn(
                        okJson("{" +
                                "  \"paymentId\": \"2222\"," +
                                "  \"paymentResponseStatus\": \"SUCCESS\"" +
                                "}")));

        // When
        final BookingResponse bookingResponse = bookingService.payForBooking("1111", new CreditCard("1234-1234-1234-1234", LocalDate.of(2018, 2, 1), new BigDecimal("20.55")));

        // Then
        assertThat(bookingResponse).isEqualTo(new BookingResponse("1111", "2222", SUCCESS));
    }
}
