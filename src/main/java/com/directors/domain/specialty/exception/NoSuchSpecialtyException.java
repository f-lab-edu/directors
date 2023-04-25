package com.directors.domain.specialty.exception;

public class NoSuchSpecialtyException extends RuntimeException {
    private final static String message = "존재하지 않는 전문분야입니다.";

    public NoSuchSpecialtyException() {
        super(message);
    }
}
