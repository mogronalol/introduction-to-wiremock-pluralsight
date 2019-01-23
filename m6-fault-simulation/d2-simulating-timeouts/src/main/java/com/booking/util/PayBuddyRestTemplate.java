package com.booking.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static java.util.Arrays.asList;

public class PayBuddyRestTemplate extends RestTemplate {

    public PayBuddyRestTemplate() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        setMessageConverters(asList(new MappingJackson2HttpMessageConverter(objectMapper)));

        final HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setConnectionRequestTimeout(100);
        requestFactory.setConnectTimeout(100);
        requestFactory.setReadTimeout(100);

        setRequestFactory(requestFactory);
    }
}
