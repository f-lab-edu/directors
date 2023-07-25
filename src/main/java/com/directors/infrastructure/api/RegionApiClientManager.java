package com.directors.infrastructure.api;

import com.directors.domain.region.Address;
import com.directors.domain.region.RegionApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RegionApiClientManager implements RegionApiClient {

    @Value("${ADDRESS_REQUEST_URL}")
    String ADDRESS_REQUEST_URL;

    @Value("${COORDINATE_TRANSFORMATION_URL}")
    String COORDINATE_TRANSFORMATION_URL;


    private final RegionApiTokenProvider regionApiTokenProvider;
    private final ApiSender apiSender;

    private final String regionOneDepth = "sido_nm";
    private final String regionTwoDepth = "sgg_nm";
    private final String regionThreeDepth = "emdong_nm";

    @Override
    public Address findRegionAddressByLocation(double latitude, double longitude) {
        var utmkCoord = fromWGS84ToUTMK(latitude, longitude);

        var uri = UriComponentsBuilder
                .fromHttpUrl(ADDRESS_REQUEST_URL)
                .queryParam("x_coor", utmkCoord.x)
                .queryParam("y_coor", utmkCoord.y)
                .queryParam("addr_type", 21)
                .queryParam("accessToken", regionApiTokenProvider.getApiAccessToken())
                .build();

        var response = apiSender.send(HttpMethod.GET, uri);

        // TODO: 토큰 유효 기간 주기(2시간)에 맞추어 토큰을 새로 가져오는 스케줄링 로직이 필요. 현재는 토큰이 유효하지 않으면 토큰을 업데이트하도록 하고 있음.
        if (!ApiResponseValidator.isCertificated(response)) {
            regionApiTokenProvider.fetchApiAccessTokenFromAPI();
            response = apiSender.send(HttpMethod.GET, uri);
        }

        ApiResponseValidator.checkNotFound(response);

        return getAddressFromResponse(response);
    }

    private UTMKCoordinate fromWGS84ToUTMK (double latitude, double longitude) {
        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(COORDINATE_TRANSFORMATION_URL)
                .queryParam("src", "EPSG:4326") // to   경위도
                .queryParam("dst", "EPSG:5179") // from UTM-K
                .queryParam("posX", longitude)
                .queryParam("posY", latitude)
                .queryParam("accessToken", regionApiTokenProvider.getApiAccessToken())
                .build();

        var response = apiSender.send(HttpMethod.GET, uri);

        if (!ApiResponseValidator.isCertificated(response)) {
            regionApiTokenProvider.fetchApiAccessTokenFromAPI();
            response = apiSender.send(HttpMethod.GET, uri);
        }

        ApiResponseValidator.checkNotFound(response);

        Map<String, Double> resultMap = (Map<String, Double>) response.getBody().get("result");

        return new UTMKCoordinate(resultMap.get("posX"), resultMap.get("posY"));
    }

    private Address getAddressFromResponse(ResponseEntity<Map<String, Object>> response) {
        var result = (ArrayList<Object>) response.getBody().get("result");
        var resultMap = (Map<String, String>) result.get(0);

        var fullAddress = resultMap.get(regionOneDepth) + " " + resultMap.get(regionTwoDepth) + " " + resultMap.get(regionThreeDepth);
        var unitAddress = resultMap.get(regionThreeDepth);

        return new Address(fullAddress, unitAddress);
    }

    private static class UTMKCoordinate {
        double x;
        double y;

        public UTMKCoordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
