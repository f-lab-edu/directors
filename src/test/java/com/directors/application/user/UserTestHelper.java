package com.directors.application.user;

import com.directors.presentation.user.request.LogInRequest;
import com.directors.presentation.user.request.SignUpRequest;

public class UserTestHelper {
    public static LogInRequest createLogInRequest(String userId, String password) {
        return LogInRequest.builder()
                .userId(userId)
                .password(password)
                .build();
    }

    public static SignUpRequest createSignUpRequest(String userId, String password, String name, String nickname, String email, String phoneNumber) {
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
