package com.directors.application.region;

import com.directors.IntegrationTestSupport;
import com.directors.application.user.AuthenticateRegionService;
import com.directors.application.user.SignUpService;
import com.directors.domain.region.Address;
import com.directors.domain.region.RegionApiClient;
import com.directors.domain.user.exception.UserRegionNotFoundException;
import com.directors.presentation.region.response.NearestAddressResponse;
import com.directors.presentation.user.request.AuthenticateRegionRequest;
import com.directors.presentation.user.request.SignUpRequest;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RegionServiceTest extends IntegrationTestSupport {

    @Autowired
    RegionService regionService;

    @Autowired
    SignUpService signUpService;

    @MockBean
    RegionApiClient regionApiClient;

    @Autowired
    AuthenticateRegionService authenticateRegionService;

    @DisplayName("지역 인증된 유저 근처의 지역을 조회한다.")
    @Test
    void getNearestAddress() {
        // given
        String givenUserId = "cnsong1234";
        double givenLatitude = 961487;
        double givenLongitude = 1949977;
        String givenFullAddress = "서울특별시 성동구 송정동";
        String givenUnitAddress = "송정동";

        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        when(regionApiClient.findRegionAddressByLocation(any(Double.class), any(Double.class)))
                .thenReturn(new Address(givenFullAddress, givenUnitAddress));

        AuthenticateRegionRequest request = AuthenticateRegionRequest.builder()
                .latitude(givenLatitude)
                .longitude(givenLongitude)
                .build();
        authenticateRegionService.authenticate(request, givenUserId);

        // when
        List<NearestAddressResponse> nearestAddress = regionService.getNearestAddress(1, givenUserId);

        // then
        assertThat(nearestAddress).hasSize(3)
                .extracting("fullAddress", "unitAddress")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("서울특별시 광진구 군자동", "군자동"),
                        Tuple.tuple("서울특별시 광진구 화양동", "화양동"),
                        Tuple.tuple("서울특별시 성동구 송정동", "송정동")
                );
    }

    @DisplayName("지역 인증되지 않은 유저 근처의 지역을 조회하면 예외가 발생한다.")
    @Test
    void getNearestAddressWithNotAuthenticatedUser() {
        // given
        String givenUserId = "cnsong1234";

        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        // when then
        assertThatThrownBy(() -> regionService.getNearestAddress(1, givenUserId))
                .isInstanceOf(UserRegionNotFoundException.class)
                .hasMessage("유저의 지역 인증 내역이 존재하지 않습니다. 먼저 지역 인증을 진행해주세요.");
    }

    @DisplayName("지역 인증된 유저 근처의 지역 아이디를 조회한다.")
    @Test
    void getNearestRegionId() {
        // given
        String givenUserId = "cnsong1234";
        double givenLatitude = 961487;
        double givenLongitude = 1949977;
        String givenFullAddress = "서울특별시 성동구 송정동";
        String givenUnitAddress = "송정동";

        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        when(regionApiClient.findRegionAddressByLocation(any(Double.class), any(Double.class)))
                .thenReturn(new Address(givenFullAddress, givenUnitAddress));

        AuthenticateRegionRequest request = AuthenticateRegionRequest.builder()
                .latitude(givenLatitude)
                .longitude(givenLongitude)
                .build();
        authenticateRegionService.authenticate(request, givenUserId);

        // when
        List<Long> nearestRegionIds = regionService.getNearestRegionId(1, givenUserId);

        // then
        assertThat(nearestRegionIds).hasSize(3).isEqualTo(List.of(11458L, 11459L, 11472L));
    }

    @DisplayName("지역 인증되지 않은 유저 근처의 지역을 조회하면 예외가 발생한다.")
    @Test
    void getNearestRegionIdWithNotAuthenticatedUser() {
        // given
        String givenUserId = "cnsong1234";

        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        // when then
        assertThatThrownBy(() -> regionService.getNearestRegionId(1, givenUserId))
                .isInstanceOf(UserRegionNotFoundException.class)
                .hasMessage("유저의 지역 인증 내역이 존재하지 않습니다. 먼저 지역 인증을 진행해주세요.");
    }

    protected static SignUpRequest createSignUpRequest(String userId, String password, String name, String nickname, String email, String phoneNumber) {
        return SignUpRequest.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .nickname(nickname)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
    }
}