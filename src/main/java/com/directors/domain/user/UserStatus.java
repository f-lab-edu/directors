package com.directors.domain.user;

public enum UserStatus {
    JOINED("가입 상태"),
    WITHDRAWN("탈퇴 상태");

    private final String value;

    UserStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
