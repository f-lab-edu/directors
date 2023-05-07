package com.directors.infrastructure.exception.common;

import com.directors.infrastructure.exception.ExceptionCode;

public class EntityNotFoundException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public EntityNotFoundException(ExceptionCode exceptionCode) {
        super();
        this.exceptionCode = exceptionCode;
    }
}
