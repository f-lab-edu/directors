package com.directors.learningTest.geoApiTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:test-application.properties")
public class GetAdministrativeDistrictTest {

    @Value("${test.consumer_key}")
    String consumer_key;

    @Value("${test.consumer_secret}")
    String consumer_secret;

    static final String tokenRequestUrl = "https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json";
    //    static final String 지오코딩RequestUrl = "https://sgisapi.kostat.go.kr/OpenAPI3/addr/geocode.json";
    static final String 리버스지오코딩RequestUrl = "https://sgisapi.kostat.go.kr/OpenAPI3/addr/rgeocode.json";

    static RestTemplate restTemplate = null;
    static HttpEntity<String> entity = null;
    static String accessToken = null;

    @BeforeEach
    public void setUp() {
        restTemplate = new RestTemplate(); // Template 객체 생성

        HttpHeaders header = new HttpHeaders();         // 헤더 생성
        entity = new HttpEntity<>(header);

        UriComponents uri = UriComponentsBuilder  // 요청 uri 정의
                .fromHttpUrl(tokenRequestUrl)
                .queryParam("consumer_key", consumer_key)
                .queryParam("consumer_secret", consumer_secret)
                .build();

        // 액세스 토큰 획득
        ResponseEntity<Map> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);
        Map<String, String> resultMap = (Map<String, String>) response.getBody().get("result");

        accessToken = resultMap.get("accessToken");
    }

    @Test
    public void 좌표에_대한_행정동_획득_동기반_지역명_API요청_성공테스트() {
        // GIVEN
        long x = 961487;  // UTM-K 기반 x, y 좌표
        long y = 1949977;
        UriComponents uri = UriComponentsBuilder  // 요청 uri 정의
                .fromHttpUrl(리버스지오코딩RequestUrl)
                .queryParam("x_coor", x)
                .queryParam("y_coor", y)
                .queryParam("addr_type", 21)
                .queryParam("accessToken", accessToken)
                .build();

        // WHEN
        ResponseEntity<Map> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);

        // THEN
        assertThat(response.getStatusCode().toString()).isEqualTo("200 OK");

        ArrayList<Object> result = (ArrayList<Object>) response.getBody().get("result");
        Map<String, String> resultMap = (Map<String, String>) result.get(0);

        assertThat(resultMap.get("full_addr")).isEqualTo("서울특별시 성동구 송정동 66-263");
        assertThat(resultMap.get("sido_nm")).isEqualTo("서울특별시");
        assertThat(resultMap.get("sgg_nm")).isEqualTo("성동구");
        assertThat(resultMap.get("emdong_nm")).isEqualTo("송정동");
    }

    @Test
    public void 좌표에_대한_행정동_획득_동이아닌_지역명_API요청_성공테스트() {
        // GIVEN
        double x = 1037662.3752666;
        double y = 1934429.11068789;

        UriComponents uri = UriComponentsBuilder  // 요청 uri 정의
                .fromHttpUrl(리버스지오코딩RequestUrl)
                .queryParam("x_coor", x)
                .queryParam("y_coor", y)
                .queryParam("addr_type", 21)
                .queryParam("accessToken", accessToken)
                .build();

        // WHEN
        ResponseEntity<Map> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);

        // THEN
        assertThat(response.getStatusCode().toString()).isEqualTo("200 OK");

        ArrayList<Object> result = (ArrayList<Object>) response.getBody().get("result");
        Map<String, String> resultMap = (Map<String, String>) result.get(0);

        assertThat(resultMap.get("full_addr")).isEqualTo("강원도 원주시 호저면 1328-1");
        assertThat(resultMap.get("sido_nm")).isEqualTo("강원도");
        assertThat(resultMap.get("sgg_nm")).isEqualTo("원주시");
        assertThat(resultMap.get("emdong_nm")).isEqualTo("호저면");
    }
}
