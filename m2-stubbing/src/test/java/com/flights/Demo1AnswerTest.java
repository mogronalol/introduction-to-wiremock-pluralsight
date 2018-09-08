package com.flights;

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

public class Demo1AnswerTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private BookingService bookingService;

    @Before
    public void setUp() {
        bookingService = new BookingService(new PayBuddyGateway("localhost", wireMockRule.port()));
    }

    @Test
    public void shouldPayForBooking() {
        // Given
        stubFor(any(anyUrl()).willReturn(okJson("{" +
                "  \"paymentId\": \"2222\"," +
                "  \"paymentResponseStatus\": \"SUCCESS\"" +
                "}")));

        // When
        final BookingResponse bookingResponse = bookingService.payForBooking("1111", "1234-1234-1234-1234", LocalDate.of(2018, 2, 1), new BigDecimal("20.55"));

        // Then
        assertThat(bookingResponse).isEqualTo(new BookingResponse("1111", "2222", SUCCESS));
    }

}
