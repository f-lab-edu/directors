package com.directors.application.user;

import com.directors.domain.user.User;
import com.directors.domain.user.UserStatus;
import com.directors.domain.user.exception.AuthenticationFailedException;
import com.directors.presentation.user.request.*;
import com.directors.presentation.user.response.LogInResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UpdateUserServiceTest extends UserTestSupport {

    @DisplayName("패스워드를 수정한다.")
    @Test
    void updatePassword() {
        // given
        String givenUserId = "cnsong1234";
        String oldPassword = "1234567890";
        String newPassword = "987654321";

        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, oldPassword, "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        UpdatePasswordRequest request = createUpdatePasswordRequest(oldPassword, newPassword);

        // when
        updateUserService.updatePassword(request, givenUserId);

        // then
        assertFalse(Thread.currentThread().isInterrupted());

        LogInRequest loginRequest = createLogInRequest(givenUserId, newPassword);
        LogInResponse logInResponse = authenticationService.logIn(loginRequest);

        assertThat(logInResponse).isNotNull();
        assertThat(logInResponse.accessToken()).isNotBlank();
        assertThat(logInResponse.refreshToken()).isNotBlank();
    }

    @DisplayName("잘못된 기존 패스워드로 수정 요청 시 예외가 발생한다.")
    @Test
    void updatePasswordWithWrongDefaultPassword() {
        // given
        String givenUserId = "cnsong1234";
        String oldPassword = "1234567890";
        String newPassword = "987654321";

        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, oldPassword, "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        UpdatePasswordRequest request = createUpdatePasswordRequest(newPassword, newPassword);

        // when then
        assertThatThrownBy(() -> updateUserService.updatePassword(request, givenUserId))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessage("유저 인증이 실패했습니다.");
    }

    @DisplayName("탈퇴한 회원의 토큰을 이용해 패스워드 수정 요청 시 예외가 발생한다.")
    @Test
    void updatePasswordWithNotRegisteredUserId() {
        // given
        String givenUserId = "cnsong1234";
        String oldPassword = "1234567890";
        String newPassword = "987654321";

        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, oldPassword, "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(signUpRequest);

        WithdrawRequest withdrawRequest = createWithdrawRequest(givenUserId, oldPassword);
        withdrawService.withdraw(withdrawRequest, givenUserId);

        UpdatePasswordRequest request = createUpdatePasswordRequest(oldPassword, newPassword);

        // when then
        assertThatThrownBy(() -> updateUserService.updatePassword(request, givenUserId))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessage("유저 인증이 실패했습니다.");
    }

    @DisplayName("이메일을 수정한다.")
    @Test
    void updateEmail() {
        // given
        String givenUserId = "cnsong1234";
        String oldEmail = "cnsong0229@gmail.com";
        String newEmail = "songsong@gmail.com";

        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567879", "송은석", "cnsong0229", oldEmail, "01077021045");
        signUpService.signUp(signUpRequest);

        UpdateEmailRequest updateEmailRequest = new UpdateEmailRequest(newEmail);

        // when
        updateUserService.updateEmail(updateEmailRequest, givenUserId);

        // then
        assertFalse(Thread.currentThread().isInterrupted());

        User user = userRepository.findByIdAndUserStatus(givenUserId, UserStatus.JOINED)
                .orElseThrow(null);

        assertThat(user.getEmail()).isEqualTo(newEmail);
    }

    @DisplayName("탈퇴한 회원의 토큰을 이용해 이메일 수정 요청 시 예외가 발생한다.")
    @Test
    void updateEmailWithNotRegisteredUserId() {
        // given
        String givenUserId = "cnsong1234";
        String oldEmail = "cnsong0229@gmail.com";
        String newEmail = "songsong@gmail.com";

        SignUpRequest signUpRequest = createSignUpRequest(givenUserId, "1234567879", "송은석", "cnsong0229", oldEmail, "01077021045");
        signUpService.signUp(signUpRequest);

        WithdrawRequest withdrawRequest = createWithdrawRequest(givenUserId, "1234567879");
        withdrawService.withdraw(withdrawRequest, givenUserId);

        UpdateEmailRequest updateEmailRequest = new UpdateEmailRequest(newEmail);

        // when then
        assertThatThrownBy(() -> updateUserService.updateEmail(updateEmailRequest, givenUserId))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessage("유저 인증이 실패했습니다.");
    }

    private static UpdatePasswordRequest createUpdatePasswordRequest(String oldPassword, String newPassword) {
        return UpdatePasswordRequest.builder()
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .build();
    }
}