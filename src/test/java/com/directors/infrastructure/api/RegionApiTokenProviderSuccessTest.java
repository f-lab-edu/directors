package com.directors.infrastructure.api;

import com.directors.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


class RegionApiTokenProviderSuccessTest extends IntegrationTestSupport {

    @Autowired
    private RegionApiTokenProvider regionApiTokenProvider;

    @DisplayName("api 키를 통해 sgis로부터 인증 토큰을 발급 받는다.")
    @Test
    void fetchApiAccessTokenFromAPI() {
        // given when
        String token = regionApiTokenProvider.fetchApiAccessTokenFromAPI();

        // then
        assertThat(token).isNotNull();
    }
}