package com.directors.infrastructure.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class RegionApiTokenProvider {
    @Value("${CONSUMER_KEY}")
    String CONSUMER_KEY;

    @Value("${CONSUMER_SECRET}")
    String CONSUMER_SECRET;

    @Value("${TOKEN_REQUEST_URL}")
    String TOKEN_REQUEST_URL;

    String apiAccessToken;

    // TODO: 04.04 토큰의 유효 시간은 2시간이므로 2시간 마다 혹은 토큰에 의한 예외 발생 시 다시 토큰을 받아올 수 있는 로직이 필요.
    private void fetchApiAccessTokenFromAPI() {
        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(TOKEN_REQUEST_URL)
                .queryParam("consumer_key", CONSUMER_KEY)
                .queryParam("consumer_secret", CONSUMER_SECRET)
                .build();

        ResponseEntity<Map> response = ApiSender.send(HttpMethod.GET, uri);

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
