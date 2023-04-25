package com.directors.domain.user.exception;

public class UserRegionNotFoundException extends RuntimeException {
    private final static String message = "유저의 지역 인증 내역이 존재하지 않습니다. 지역 인증을 진행해주세요.";
    public final String requestedUserId;

    public UserRegionNotFoundException(String requestedUserId) {
        super(message);
        this.requestedUserId = requestedUserId;
    }
}
