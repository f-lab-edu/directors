package com.directors.domain.user.exception;

public class NoSuchUserException extends RuntimeException {
    private final static String message = "존재하지 않는 유저 Id입니다.";
    public final String requestedUserId;

    public NoSuchUserException(String requestedUserId) {
        super(message);
        this.requestedUserId = requestedUserId;
    }
}
