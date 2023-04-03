package com.directors.infrastructure.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.Map;

public class ApiSender {
    public static ResponseEntity<Map> send(HttpMethod method, UriComponents uri) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(header);

        ResponseEntity<Map> response = restTemplate.exchange(uri.toString(), method, entity, Map.class);
        if (!response.getStatusCode().toString().equals("200 OK")) {
            throw new HttpClientErrorException(response.getStatusCode());
        }

        return response;
    }
}
