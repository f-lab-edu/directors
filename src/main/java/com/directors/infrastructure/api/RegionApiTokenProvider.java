package com.directors.infrastructure.api;

import com.directors.infrastructure.exception.api.ExteralApiAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class RegionApiTokenProvider {
    @Value("${CONSUMER_KEY}")
    String CONSUMER_KEY;

    @Value("${CONSUMER_SECRET}")
    String CONSUMER_SECRET;

    @Value("${TOKEN_REQUEST_URL}")
    String TOKEN_REQUEST_URL;

    String apiAccessToken;
    private final ApiSender apiSender;

    public void fetchApiAccessTokenFromAPI() {
        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(TOKEN_REQUEST_URL)
                .queryParam("consumer_key", CONSUMER_KEY)
                .queryParam("consumer_secret", CONSUMER_SECRET)
                .build();

        ResponseEntity<Map<String, Object>> response = apiSender.send(HttpMethod.GET, uri);

        validateResponse(response);

        apiAccessToken = ((Map<String, Object>) response.getBody().get("result")).get("accessToken").toString();
    }

    private static void validateResponse(ResponseEntity<Map<String, Object>> response) {
        if (response.getBody().get("errMsg").equals("인증정보가 존재하지 않습니다")) {
            throw new ExteralApiAuthenticationException("Key verification is required for SGIS API");
        }
    }

    public String getApiAccessToken() {
        if (apiAccessToken == null) {
            fetchApiAccessTokenFromAPI();
        }
        return apiAccessToken;
    }
}
