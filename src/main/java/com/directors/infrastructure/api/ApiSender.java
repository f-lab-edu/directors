package com.directors.infrastructure.api;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.Map;

@Component
public class ApiSender {
    @Retryable(retryFor = {HttpClientErrorException.class, HttpServerErrorException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ResponseEntity<Map<String, Object>> send(HttpMethod method, UriComponents uri) throws HttpClientErrorException, HttpServerErrorException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(header);

        return restTemplate.exchange(uri.toString(), method, entity, new ParameterizedTypeReference<>() {
        });
    }
}
