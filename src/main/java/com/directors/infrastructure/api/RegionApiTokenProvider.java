package com.directors.infrastructure.api;

import com.directors.infrastructure.exception.api.RenewApiKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
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

    private String apiAccessToken;
    private final ApiSender apiSender;

    public String fetchApiAccessTokenFromAPI() {
        var uri = UriComponentsBuilder
                .fromHttpUrl(TOKEN_REQUEST_URL)
                .queryParam("consumer_key", CONSUMER_KEY)
                .queryParam("consumer_secret", CONSUMER_SECRET)
                .build();

        var response = apiSender.send(HttpMethod.GET, uri);

        if (!ApiResponseValidator.isCertificated(response)) {
            throw new RenewApiKeyException();
        }

        apiAccessToken = ((Map<String, Object>) response.getBody().get("result")).get("accessToken").toString();

        return apiAccessToken;
    }

    public String getApiAccessToken() {
        if (apiAccessToken == null) {
            return fetchApiAccessTokenFromAPI();
        }
        return apiAccessToken;
    }
}
