package com.directors.infrastructure.api;

import com.directors.domain.region.Address;
import com.directors.domain.region.RegionApiClient;
import com.directors.infrastructure.exception.api.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.UriComponents;
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

        recoverIfTokenUnvalidate(uri, response);
        checkNotFound(response);

        var result = getResultFromResponse(response);

        String fullAddress = result.get("sido_nm") + " " + result.get("sgg_nm") + " " + result.get("emdong_nm");
        String unitAddress = result.get("emdong_nm");

        return new Address(fullAddress, unitAddress);
    }

    private void recoverIfTokenUnvalidate(UriComponents uri, ResponseEntity<Map<String, Object>> response) {
        if (response.getBody().get("errMsg").equals("인증 정보가 존재하지 않습니다")) {
            regionApiTokenProvider.fetchApiAccessTokenFromAPI();
            response = apiSender.send(HttpMethod.GET, uri);

            if (response == null) {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private void checkNotFound(ResponseEntity<Map<String, Object>> response) {
        if ((Integer) response.getBody().get("errCd") == -100) {
            throw new NotFoundException();
        }
    }

    private Map<String, String> getResultFromResponse(ResponseEntity<Map<String, Object>> response) {
        var result = (ArrayList<Object>) response.getBody().get("result");
        var resultMap = (Map<String, String>) result.get(0);
        return resultMap;
    }
}
