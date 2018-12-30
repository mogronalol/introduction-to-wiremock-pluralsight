package com.booking.gateway;

import com.booking.util.PayBuddyRestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

public class PayBuddyGateway {

    private final PayBuddyRestTemplate restTemplate = new PayBuddyRestTemplate();
    private final String payBuddyBaseUrl;

    public PayBuddyGateway(final String payBuddyHost, final int payBuddyPort) {
        this.payBuddyBaseUrl = String.format("http://%s:%s/", payBuddyHost, payBuddyPort);
    }

    public VATCalculation calculateVAT(BigDecimal amount) {

        final String uri = UriComponentsBuilder.fromHttpUrl(payBuddyBaseUrl)
                .path("vat")
                .queryParam("amount", amount)
                .build()
                .toUriString();

        return restTemplate.getForObject( uri, VATCalculation.class);
    }
}
