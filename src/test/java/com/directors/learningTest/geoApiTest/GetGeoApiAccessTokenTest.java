package com.directors.learningTest.geoApiTest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:test-application.properties")
public class GetGeoApiAccessTokenTest {
    @Value("${test.consumer_key}")
    String consumer_key;

    @Value("${test.consumer_secret}")
    String consumer_secret;

    static final String requesTokenUrl = "https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json";
    static RestTemplate restTemplate = null;
    static HttpEntity<String> entity = null;

    @BeforeEach
    public void setUp() {
        restTemplate = new RestTemplate(); // Template 객체 생성

        HttpHeaders header = new HttpHeaders();         // 헤더 생성
        entity = new HttpEntity<>(header);
    }

    @Disabled
    @Test
    public void 토큰획득API요청성공테스트() {
        // GIVEN
        UriComponents uri = UriComponentsBuilder  // 요청 uri 정의
                .fromHttpUrl(requesTokenUrl)
                .queryParam("consumer_key", consumer_key)
                .queryParam("consumer_secret", consumer_secret)
                .build();

        // WHEN
        ResponseEntity<Map> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);

        // THEN
        assertThat(response.getStatusCode().toString()).isEqualTo("200 OK"); // api 성공 테스트

        // 데이터가 계속 변하므로 값의 유무만 체크하도록 함.
        Map<String, String> resultMap = (Map<String, String>) response.getBody().get("result");

        assertThat(resultMap.get("accessTimeout")).isNotNull();
        assertThat(resultMap.get("accessToken")).isNotNull();
    }

    @Disabled
    @Test
    public void 토큰획득API요청실패테스트_키의부재() {
        // GIVEN
        UriComponents uri = UriComponentsBuilder  // 요청 uri 정의
                .fromHttpUrl(requesTokenUrl)
                .queryParam("consumer_key", "")
                .queryParam("consumer_secret", consumer_secret)
                .build();

        // WHEN
        HttpClientErrorException exception = Assertions.assertThrows(HttpClientErrorException.class,
                () -> restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class));

        // THEN
        assertThat(HttpStatus.PRECONDITION_FAILED).isEqualTo(exception.getStatusCode());
    }
}

