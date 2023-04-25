package com.directors.domain.user.exception;

public class DuplicateIdException extends RuntimeException {
    private final static String message = "이미 존재하는 Id입니다.";
    public final String duplicatedId;

    public DuplicateIdException(String duplicatedId) {
        super(message);
        this.duplicatedId = duplicatedId;
    }
}