package com.directors.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User {
    private String userId;

    private String password;

    private String name;

    private String nickname;

    private String email;

    private String phoneNumber;

    private String region;

    private long reward;
}
