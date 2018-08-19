package com.flights.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class PayBuddyGateway {

    private final HttpClient httpclient;
    private final ObjectMapper objectMapper;

    public PayBuddyGateway() {
        httpclient = HttpClients.createDefault();
        objectMapper = new ObjectMapper();
    }

    public PayBuddyPaymentResponse makePayment(String creditCardNumber, LocalDate creditCardExpiry, BigDecimal amount) {
        try {
            final HttpPost httpPost = toPayBuddyRequest(creditCardNumber, creditCardExpiry, amount);

            final HttpResponse response = httpclient.execute(httpPost);

            return toPaymentBuddyResponse(response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HttpPost toPayBuddyRequest(String creditCardNumber, LocalDate creditCardExpiry, BigDecimal amount) throws URISyntaxException, UnsupportedEncodingException, JsonProcessingException {
        final HttpPost httpPost = new HttpPost(new URIBuilder()
                .setScheme("http")
                .setHost("localhost")
                .setPort(8082)
                .setPath("/payments")
                .build());

        final PayBuddyPaymentRequest payBuddyGatewayRequest = new PayBuddyPaymentRequest(creditCardNumber, creditCardExpiry, amount);

        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(payBuddyGatewayRequest)));

        return httpPost;
    }

    private PayBuddyPaymentResponse toPaymentBuddyResponse(HttpResponse response) throws IOException {
        final String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        return objectMapper.readValue(responseString, PayBuddyPaymentResponse.class);
    }
}
