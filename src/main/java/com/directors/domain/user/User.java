package com.directors.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class User {
    String userId;

    String password;

    String name;

    String nickname;

    String email;

    String phoneNumber;

    String region;

    long reward;
}
