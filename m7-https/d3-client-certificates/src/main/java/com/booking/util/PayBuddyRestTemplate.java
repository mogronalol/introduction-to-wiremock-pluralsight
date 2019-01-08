package com.booking.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import static java.util.Arrays.asList;

public class PayBuddyRestTemplate extends RestTemplate {

    public PayBuddyRestTemplate() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        setMessageConverters(asList(new MappingJackson2HttpMessageConverter(objectMapper)));

        try {
            SSLContext sslContext = SSLContextBuilder
                    .create()
                    .loadKeyMaterial(ResourceUtils.getFile("classpath:client-keystore.jks"), "wiremock".toCharArray(), "wiremock".toCharArray())
                    .loadTrustMaterial(ResourceUtils.getFile("classpath:client-truststore.jks"), "wiremock".toCharArray())
                    .build();

            HttpClient client = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .build();

            setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private KeyStore loadPfx(String file, char[] password) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        File key = ResourceUtils.getFile(file);
        try (InputStream in = new FileInputStream(key)) {
            keyStore.load(in, password);
        }
        return keyStore;
    }
}
