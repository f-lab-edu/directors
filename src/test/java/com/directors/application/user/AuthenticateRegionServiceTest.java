package com.directors.application.user;

import com.directors.domain.region.Address;
import com.directors.domain.region.RegionApiClient;
import com.directors.presentation.user.request.AuthenticateRegionRequest;
import com.directors.presentation.user.request.SignUpRequest;
import com.directors.presentation.user.response.AuthenticateRegionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthenticateRegionServiceTest extends UserTestSupport {

    @MockBean
    public RegionApiClient regionApiClient;

    @DisplayName("위도와 경도 정보를 통해 유저의 지역을 인증한다.")
    @Test
    void authenticate() {
        // given
        String givenUserId = "cnsong1234";
        double givenLatitude = 961487;
        double givenLongitude = 1949977;
        String givenFullAddress = "서울특별시 성동구 송정동";
        String givenUnitAddress = "송정동";

        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        AuthenticateRegionRequest request = AuthenticateRegionRequest.builder()
                .latitude(givenLatitude)
                .longitude(givenLongitude)
                .build();

        when(regionApiClient.findRegionAddressByLocation(any(Double.class), any(Double.class)))
                .thenReturn(new Address(givenFullAddress, givenUnitAddress));

        // when
        AuthenticateRegionResponse response = authenticateRegionService.authenticate(request, givenUserId);

        // then
        assertThat(response)
                .extracting("fullAddress", "unitAddress")
                .contains(givenFullAddress, givenUnitAddress);
    }

    @DisplayName("한국 행정 구역 내에 있지 않은 위치의 위도와 경도 정보로 지역 인증을 시도할 경우 예외를 발생시킨다.")
    @Test
    void authenticateWithOutOfBoundary() {
        // given
        String givenUserId = "cnsong1234";
        double givenLatitude = 1;
        double givenLongitude = 1;

        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        AuthenticateRegionRequest request = AuthenticateRegionRequest.builder()
                .latitude(givenLatitude)
                .longitude(givenLongitude)
                .build();

        when(regionApiClient.findRegionAddressByLocation(any(Double.class), any(Double.class)))
                .thenThrow(IllegalArgumentException.class);

        // when then
        assertThatThrownBy(() -> authenticateRegionService.authenticate(request, givenUserId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}