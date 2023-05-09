package com.directors.application.user;

import com.directors.domain.user.exception.AuthenticationFailedException;
import com.directors.presentation.user.request.SignUpRequest;
import com.directors.presentation.user.request.WithdrawRequest;
import com.directors.presentation.user.response.WithdrawResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WithdrawServiceTest extends UserTestSupport {

    @DisplayName("탈퇴 요청을 통해 회원을 탈퇴 처리한다.")
    @Test
    void withdraw() {
        // given
        String givenUserId = "cnsong1234";
        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);


        WithdrawRequest withdrawRequest = createWithdrawRequest(givenUserId, "1234567890");

        // when
        WithdrawResponse response = withdrawService.withdraw(withdrawRequest, givenUserId);

        // then
        assertThat(response.userId()).isEqualTo(givenUserId);
    }

    @DisplayName("회원가입되지 않은 회원 아이디로 탈퇴 요청을 하면 예외가 발생한다.")
    @Test
    void withdrawWithNotRegisteredUserId() {
        // given
        String givenUserId = "cnsong1234";
        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        WithdrawRequest withdrawRequest = createWithdrawRequest("wrongUserId", "1234567890");

        // when then
        assertThatThrownBy(() -> withdrawService.withdraw(withdrawRequest, givenUserId))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessage("유저 인증이 실패했습니다.");
    }

    @DisplayName("이미 탈퇴한 회원 아이디로 탈퇴 요청을 하면 예외가 발생한다.")
    @Test
    void withdrawWithWithdrawalUserId() {
        // given
        String givenUserId = "cnsong1234";
        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        WithdrawRequest withdrawRequest = createWithdrawRequest(givenUserId, "1234567890");
        withdrawService.withdraw(withdrawRequest, givenUserId);

        // when then
        assertThatThrownBy(() -> withdrawService.withdraw(withdrawRequest, givenUserId))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessage("유저 인증이 실패했습니다.");
    }

    @DisplayName("탈퇴 요청한 회원 아이디와 토큰의 회원 아이디가 다르면 예외가 발생한다.")
    @Test
    void withdrawWithWrongToken() {
        // given
        String givenUserId = "cnsong1234";
        String userIdByToken = "userIdByToken";
        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        WithdrawRequest withdrawRequest = createWithdrawRequest(givenUserId, "1234567890");

        // when then
        assertThatThrownBy(() -> withdrawService.withdraw(withdrawRequest, userIdByToken))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessage("유저 인증이 실패했습니다.");
    }
}