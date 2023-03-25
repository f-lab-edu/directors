package com.directors.infrastructure.exception.user;

public class AuthenticationFailedException extends RuntimeException {
    private final static String message = "유저 인증이 실패했습니다.";
    public final String requestedUserId;

    public AuthenticationFailedException(String requestedUserId) {
        super(message);
        this.requestedUserId = requestedUserId;
    }
}
