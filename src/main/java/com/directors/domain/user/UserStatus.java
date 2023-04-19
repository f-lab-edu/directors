package com.directors.domain.user;

public enum UserStatus {
    JOINED("가입"),
    WITHDRAWN("탈퇴");

    private final String value;

    UserStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
