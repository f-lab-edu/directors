package com.directors.application.user;

import com.directors.domain.auth.Token;
import com.directors.presentation.user.request.LogInRequest;
import com.directors.presentation.user.request.LogOutRequest;
import com.directors.presentation.user.request.SignUpRequest;
import com.directors.presentation.user.response.LogInResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LogOutTest extends UserTestSupport {

    @DisplayName("로그아웃 성공 시 유저의 모든 토큰 데이터가 삭제된다.")
    @Test
    void logOut() {
        // given
        SignUpRequest signUpRequest = createSignUpRequest("cnsong1234", "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);
        LogInRequest loginRequest = createLogInRequest("cnsong1234", "1234567890");

        LogInResponse logInResponse = authenticationService.logIn(loginRequest);
        String refreshToken = logInResponse.refreshToken();

        LogOutRequest logOutRequest = new LogOutRequest(refreshToken);

        // when
        authenticationService.logOut(logOutRequest);

        // then
        boolean tokenPresent = tokenRepository.findTokenByTokenString(refreshToken).isPresent();
        assertThat(tokenPresent).isFalse();
    }

    @DisplayName("잘못된 리프레시 토큰을 통해 로그아웃하면 인증 예외가 발생한다.")
    @Test
    void logOutWithWrongRefreshToken() {
        // given
        String givenUserId = "cnsong1234";
        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);
        LogInRequest loginRequest = createLogInRequest(givenUserId, "1234567890");

        LogInResponse logInResponse = authenticationService.logIn(loginRequest);
        String refreshToken = logInResponse.refreshToken();

        LogOutRequest logOutRequest = new LogOutRequest(refreshToken + "1");

        // when then
        assertThatThrownBy(() -> authenticationService.logOut(logOutRequest))
                .isInstanceOf(JwtException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }

    @DisplayName("유효기간이 지난 리프레시 토큰을 통해 로그아웃하면 인증 예외가 발생한다.")
    @Test
    void logOutWithExpiredRefreshToken() {
        // given
        String givenUserId = "cnsong1234";
        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);
        LogInRequest loginRequest = createLogInRequest(givenUserId, "1234567890");

        String expiredRefreshToken = tokenGenerator.generateJwtTokenWithDay(givenUserId, 0);

        Token token = new Token(expiredRefreshToken, givenUserId, new Date());
        tokenRepository.saveToken(token);

        LogOutRequest logOutRequest = new LogOutRequest(expiredRefreshToken);

        // when then
        assertThatThrownBy(() -> authenticationService.logOut(logOutRequest))
                .isInstanceOf(ExpiredJwtException.class)
                .hasMessageContaining("JWT expired at");
    }
}
