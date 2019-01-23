package com.booking.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

import static java.util.Arrays.asList;

public class PayBuddyRestTemplate extends RestTemplate {

    public PayBuddyRestTemplate() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        setMessageConverters(asList(new MappingJackson2HttpMessageConverter(objectMapper)));

        try {
            final SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial(
                            ResourceUtils.getFile(
                                    "classpath:client-truststore.jks"),
                            "password".toCharArray())
                    .build();

            final CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .build();

            setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
