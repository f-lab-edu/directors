package com.directors.application.user;

import com.directors.presentation.user.request.LogInRequest;
import com.directors.presentation.user.request.RefreshAuthenticationRequest;
import com.directors.presentation.user.request.SignUpRequest;
import com.directors.presentation.user.response.LogInResponse;
import com.directors.presentation.user.response.RefreshAuthenticationResponse;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RefreshAuthenticationTest extends UserTestSupport {

    @DisplayName("액세스 토큰과 리프레시 토큰을 통해 토큰을 재발급받는다.")
    @Test
    void refreshAuthentication() {
        // given
        String givenUserId = "cnsong1234";
        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);
        LogInRequest loginRequest = createLogInRequest(givenUserId, "1234567890");

        LogInResponse logInResponse = authenticationService.logIn(loginRequest);

        RefreshAuthenticationRequest request = new RefreshAuthenticationRequest(logInResponse.accessToken(), logInResponse.refreshToken());

        // when
        RefreshAuthenticationResponse response = authenticationService.refreshAuthentication(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();

        // access token validation
        String accessToken = logInResponse.accessToken();
        Authentication authenticationByAccessToken = jwtAuthenticationManager.getAuthentication(accessToken);

        String accessPrincipal = (String) authenticationByAccessToken.getPrincipal();
        long expirationDayByAccessToken = jwtAuthenticationManager.getExpirationDayByToken(accessToken);

        assertThat(accessPrincipal).isEqualTo(givenUserId);
        assertThat(expirationDayByAccessToken).isEqualTo(0);

        // refresh token validation
        String refreshToken = logInResponse.refreshToken();
        Authentication authenticationByRefreshToken = jwtAuthenticationManager.getAuthentication(refreshToken);
        String refreshPrincipal = (String) authenticationByRefreshToken.getPrincipal();
        long expirationDayByRefreshToken = jwtAuthenticationManager.getExpirationDayByToken(refreshToken);

        assertThat(refreshPrincipal).isEqualTo(givenUserId);
        assertThat(expirationDayByRefreshToken).isEqualTo(29);
    }

    @DisplayName("잘못된 내용의 토큰을 통해 토큰을 재발급하려고 하면 예외가 발생한다.")
    @Test
    void refreshAuthenticationWithWrongTokens() {
        // given
        String givenUserId = "cnsong1234";
        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);
        LogInRequest loginRequest = createLogInRequest(givenUserId, "1234567890");

        LogInResponse logInResponse = authenticationService.logIn(loginRequest);

        RefreshAuthenticationRequest request = new RefreshAuthenticationRequest(logInResponse.accessToken() + "1", logInResponse.refreshToken() + "2");

        // when then
        assertThatThrownBy(() -> authenticationService.refreshAuthentication(request))
                .isInstanceOf(JwtException.class)
                .hasMessage("JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");
    }

    @DisplayName("유효기간이 지난 토큰을 통해 토큰을 재발급하려고 하면 예외가 발생한다.")
    @Test
    void refreshAuthenticationWithExpiredToken() {
        // given
        String givenUserId = "cnsong1234";
        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        String expiredAccessToken = tokenGenerator.generateJwtTokenWithDay(givenUserId, 0);
        String expiredRefreshToken = tokenGenerator.generateJwtTokenWithDay(givenUserId, 6);
        RefreshAuthenticationRequest request = new RefreshAuthenticationRequest(expiredAccessToken, expiredRefreshToken);

        // when then
        assertThatThrownBy(() -> authenticationService.refreshAuthentication(request))
                .isInstanceOf(JwtException.class)
                .hasMessageContaining("JWT expired at");
    }

    @DisplayName("로그인 상태가 아닌 채로 토큰을 재발급하려고 하면 예외가 발생한다.")
    @Test
    void refreshAuthenticationWithOutLogin() {
        // given
        String givenUserId = "cnsong1234";
        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        String expiredAccessToken = tokenGenerator.generateJwtTokenWithDay(givenUserId, 2);
        String expiredRefreshToken = tokenGenerator.generateJwtTokenWithDay(givenUserId, 8);
        RefreshAuthenticationRequest request = new RefreshAuthenticationRequest(expiredAccessToken, expiredRefreshToken);

        // when then
        assertThatThrownBy(() -> authenticationService.refreshAuthentication(request))
                .isInstanceOf(JwtException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }
}
