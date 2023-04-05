package com.directors.infrastructure.api;

import com.directors.infrastructure.exception.api.ExteralApiAuthenticationException;
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
public class RegionApiTokenProvider {
    @Value("${CONSUMER_KEY}")
    String CONSUMER_KEY;

    @Value("${CONSUMER_SECRET}")
    String CONSUMER_SECRET;

    @Value("${TOKEN_REQUEST_URL}")
    String TOKEN_REQUEST_URL;

    String apiAccessToken;

    public void fetchApiAccessTokenFromAPI() {
        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(TOKEN_REQUEST_URL)
                .queryParam("consumer_key", CONSUMER_KEY)
                .queryParam("consumer_secret", CONSUMER_SECRET)
                .build();

        ResponseEntity<Map> response = ApiSender.send(HttpMethod.GET, uri);

        if (response.getBody().get("errMsg").equals("인증 정보가 존재하지 않습니다")) {
            throw new ExteralApiAuthenticationException("Key verification is required for SGIS API");
        }

        Map<String, String> resultMap = (Map<String, String>) response.getBody().get("result");
        apiAccessToken = resultMap.get("accessToken");
    }

    public String getApiAccessToken() {
        if (apiAccessToken == null) {
            fetchApiAccessTokenFromAPI();
        }
        return apiAccessToken;
    }
}
