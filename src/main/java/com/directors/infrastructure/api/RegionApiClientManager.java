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

    private final RegionApiTokenProvider regionApiTokenProvider;

    @Value("${ADDRESS_REQUEST_URL}")
    String ADDRESS_REQUEST_URL;

    @Override
    public Address findRegionAddressByLocation(double latitude, double longitude) {
        //  TODO : 04.04 유효하지 않은 토큰에 대한 예외나, 기타 필요한 예외가 있는지 확인하고 핸들러에 추가할 수 있어야 함.
        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(ADDRESS_REQUEST_URL)
                .queryParam("x_coor", latitude)
                .queryParam("y_coor", longitude)
                .queryParam("addr_type", 21)
                .queryParam("accessToken", regionApiTokenProvider.getApiAccessToken())
                .build();

        ResponseEntity<Map> response = ApiSender.send(HttpMethod.GET, uri);

        ArrayList<Object> result = (ArrayList<Object>) response.getBody().get("result");
        Map<String, String> resultMap = (Map<String, String>) result.get(0);

        String fullAddress = resultMap.get("sido_nm") + " " + resultMap.get("sgg_nm") + " " + resultMap.get("emdong_nm");

        return new Address(fullAddress, resultMap.get("emdong_nm"));
    }
}
