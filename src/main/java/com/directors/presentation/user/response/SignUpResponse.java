package com.directors.presentation.user.response;

import com.directors.domain.user.User;
import lombok.Builder;

@Builder
public record SignUpResponse(
        String id,
        String name,
        String nickname,
        String email,
        String phoneNumber
) {
    public static SignUpResponse of(User user) {
        return SignUpResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
