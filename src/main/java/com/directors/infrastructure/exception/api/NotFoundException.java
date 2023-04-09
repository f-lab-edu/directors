package com.directors.infrastructure.exception.api;

public class NotFoundException extends RuntimeException {
    private final static String message = "검색 결과가 존재하지 않습니다.";

    public NotFoundException() {
        super(message);
    }
}
