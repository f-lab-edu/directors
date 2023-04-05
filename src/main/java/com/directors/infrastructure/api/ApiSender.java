package com.directors.infrastructure.api;

import com.directors.infrastructure.exception.api.ExternalApiServerException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.Map;

public class ApiSender {

    @Retryable(retryFor = {HttpClientErrorException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public static ResponseEntity<Map> send(HttpMethod method, UriComponents uri) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(header);

        ResponseEntity<Map> response = null;

        try {
            response = restTemplate.exchange(uri.toString(), method, entity, Map.class);
        } catch (HttpClientErrorException e) {
            if (!e.getStatusCode().is2xxSuccessful()) {
                throw new HttpClientErrorException(e.getStatusCode(), "need to check api request parameter.");
            }
        } catch (HttpServerErrorException e) {
            throw new ExternalApiServerException();
        }

        return response;
    }
}
