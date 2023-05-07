package com.directors.infrastructure.api;

import com.directors.infrastructure.exception.api.RenewApiKeyException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Disabled
@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource(properties = {
        "CONSUMER_KEY=WRONG_KEY",
        "CONSUMER_SECRET=WRONG_SECRET",
})
public class RegionApiTokenProviderFailTest {

    @Autowired
    private RegionApiTokenProvider regionApiTokenProvider;

    @DisplayName("잘못된 api 키를 통해 인증 토큰을 발급 받으려 할 경우 예외가 발생한다.")
    @Test
    void fetchApiAccessTokenFromAPIWithWrongKey() {
        assertThatThrownBy(() -> regionApiTokenProvider.fetchApiAccessTokenFromAPI())
                .isInstanceOf(RenewApiKeyException.class);
    }
}
