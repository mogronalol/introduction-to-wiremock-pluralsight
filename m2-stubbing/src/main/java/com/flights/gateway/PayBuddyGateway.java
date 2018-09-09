package com.flights.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class PayBuddyGateway {

    private final RestTemplate restTemplate;
    private final String payBuddyBaseUrl;

    public PayBuddyGateway(final String payBuddyHost, final int payBuddyPort) {
        this.payBuddyBaseUrl = String.format("http://%s:%s/", payBuddyHost, payBuddyPort);
        final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        restTemplate = new RestTemplate(Arrays.asList(new MappingJackson2HttpMessageConverter(objectMapper)));
    }

    public PayBuddyPaymentResponse makePayment(String creditCardNumber, LocalDate creditCardExpiry, BigDecimal amount) {
        final PayBuddyPaymentRequest request = new PayBuddyPaymentRequest(creditCardNumber, creditCardExpiry, amount);
        return restTemplate.postForObject(payBuddyBaseUrl + "/payments", request, PayBuddyPaymentResponse.class);
    }

    public PayBuddyFraudCheckResponse fraudCheck(String creditCardNumber) {
        return restTemplate.getForObject(payBuddyBaseUrl + "/blacklisted-cards/" + creditCardNumber, PayBuddyFraudCheckResponse.class);
    }
}
