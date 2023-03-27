package com.directors.application.user;

import com.directors.domain.user.User;
import com.directors.domain.user.UserStatus;
import com.directors.presentation.user.request.SignUpRequest;

import java.util.Date;

public class UserConverter {
    public static User toEntity(SignUpRequest signUpRequest) {
        // region은 회원 가입 후, 추가로 설정할 수 있는 로직을 만들 예정입니다.
        return new User(
                signUpRequest.userId(),
                signUpRequest.password(),
                signUpRequest.name(),
                signUpRequest.nickname(),
                signUpRequest.email(),
                signUpRequest.phoneNumber(),
                "", 0, UserStatus.JOINED, new Date(), null);
    }
}
