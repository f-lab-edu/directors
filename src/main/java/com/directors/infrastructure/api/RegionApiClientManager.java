package com.directors.infrastructure.api;

import com.directors.domain.region.Address;
import com.directors.domain.region.RegionApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RegionApiClientManager implements RegionApiClient {

    @Value("${ADDRESS_REQUEST_URL}")
    String ADDRESS_REQUEST_URL;

    private final RegionApiTokenProvider regionApiTokenProvider;
    private final ApiSender apiSender;

    private final String regionOneDepth = "sido_nm";
    private final String regionTwoDepth = "sgg_nm";
    private final String regionThreeDepth = "emdong_nm";

    @Override
    public Address findRegionAddressByLocation(double latitude, double longitude) {
        var uri = UriComponentsBuilder
                .fromHttpUrl(ADDRESS_REQUEST_URL)
                .queryParam("x_coor", latitude)
                .queryParam("y_coor", longitude)
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

    private Address getAddressFromResponse(ResponseEntity<Map<String, Object>> response) {
        var result = (ArrayList<Object>) response.getBody().get("result");
        var resultMap = (Map<String, String>) result.get(0);

        var fullAddress = resultMap.get(regionOneDepth) + " " + resultMap.get(regionTwoDepth) + " " + resultMap.get(regionThreeDepth);
        var unitAddress = resultMap.get(regionThreeDepth);

        return new Address(fullAddress, unitAddress);
    }
}
