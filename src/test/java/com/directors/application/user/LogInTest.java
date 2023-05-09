package com.directors.application.user;

import com.directors.domain.user.User;
import com.directors.domain.user.UserStatus;
import com.directors.domain.user.exception.AuthenticationFailedException;
import com.directors.presentation.user.request.LogInRequest;
import com.directors.presentation.user.request.SignUpRequest;
import com.directors.presentation.user.response.LogInResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LogInTest extends UserTestSupport {

    @DisplayName("아이디와 패스워드를 통해 로그인을 한다.")
    @Test
    void logIn() {
        // given
        String givenUserId = "cnsong1234";
        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        LogInRequest loginRequest = createLogInRequest(givenUserId, "1234567890");

        // when
        LogInResponse logInResponse = authenticationService.logIn(loginRequest);

        // then
        assertThat(logInResponse).isNotNull();
        assertThat(logInResponse.accessToken()).isNotBlank();
        assertThat(logInResponse.refreshToken()).isNotBlank();

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

    @DisplayName("회원 가입되지 않은 아이디를 통해 로그인을 하면 인증 예외가 발생한다.")
    @Test
    void logInWithWrongId() {
        // given
        LogInRequest givenLoginRequest = createLogInRequest("cnsong1234", "1234567890");

        // when then
        assertThatThrownBy(() -> authenticationService.logIn(givenLoginRequest))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessage("유저 인증이 실패했습니다.");
    }

    @DisplayName("잘못된 비밀번호를 통해 로그인을 하면 인증 예외가 발생한다.")
    @Test
    void logInWithWrongPassword() {
        // given
        String givenUserId = "cnsong1234";
        String givenPassword = "1234567890";

        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, givenPassword, "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        LogInRequest givenLoginRequest = createLogInRequest(givenUserId, givenPassword + "1");

        // when then
        assertThatThrownBy(() -> authenticationService.logIn(givenLoginRequest))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessage("유저 인증이 실패했습니다.");
    }

    @DisplayName("탈퇴한 계정의 아이디와 비밀번호를 통해 로그인을 하면 인증 예외가 발생한다.")
    @Test
    void logInWithWithdrawlAccount() {
        // given
        String givenUserId = "cnsong1234";
        String givenPassword = "1234567890";
        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, givenPassword, "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        User user = userRepository
                .findByIdAndUserStatus(givenUserId, UserStatus.JOINED)
                .orElseThrow(null);

        user.withdrawal(LocalDateTime.now());

        LogInRequest givenLoginRequest = createLogInRequest(givenUserId, givenPassword);

        // when then
        assertThatThrownBy(() -> authenticationService.logIn(givenLoginRequest))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessage("유저 인증이 실패했습니다.");
    }
}