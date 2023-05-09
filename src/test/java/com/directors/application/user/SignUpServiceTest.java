package com.directors.application.user;

import com.directors.domain.user.exception.DuplicateIdException;
import com.directors.presentation.user.request.SignUpRequest;
import com.directors.presentation.user.response.SignUpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SignUpServiceTest extends UserTestSupport {

    @DisplayName("회원 정보를 통해 회원 가입을 한다.")
    @Test
    void signUp() {
        // given
        SignUpRequest request = createSignUpRequest("cnsong1234", "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");

        // when
        SignUpResponse signUpResponse = signUpService.signUp(request);

        // then
        assertThat(signUpResponse.id()).isNotNull();
        assertThat(signUpResponse)
                .extracting("id", "name", "nickname", "email", "phoneNumber")
                .contains("cnsong1234", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
    }

    @DisplayName("기존에 가입된 아이디 있을 경우 회원 가입을 할 수 없다.")
    @Test
    void signUpWithDuplicateId() {
        // given
        SignUpRequest request = createSignUpRequest("cnsong1234", "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(request);

        SignUpRequest duplicateRequest = createSignUpRequest("cnsong1234", "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");

        // when then
        assertThatThrownBy(() -> signUpService.signUp(duplicateRequest))
                .isInstanceOf(DuplicateIdException.class)
                .hasMessage("이미 존재하는 Id입니다.");
    }

    @DisplayName("기존에 아이디가 있는 경우 예외가 발생한다.")
    @Test
    void isDuplicatedUser() {
        // given
        String givenId = "cnsong1234";

        SignUpRequest request = createSignUpRequest(givenId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(request);

        // when then
        assertThatThrownBy(() -> signUpService.isDuplicatedUser(givenId))
                .isInstanceOf(DuplicateIdException.class)
                .hasMessage("이미 존재하는 Id입니다.");
    }

    @DisplayName("기존에 아이디가 없는 경우 예외가 발생하지 않는다.")
    @Test
    void isDuplicatedUserWithNotRegisteredUserId() {
        // given
        String givenId = "cnsong1234";
        String notRegisteredUserId = "songsong";

        SignUpRequest request = createSignUpRequest(givenId, "1234567890", "송은석", "cnsong0229", "thddmstjrwkd@naver.com", "01077021045");
        signUpService.signUp(request);

        // when
        signUpService.isDuplicatedUser(notRegisteredUserId);

        // then
        assertFalse(Thread.currentThread().isInterrupted());
    }

}