package com.directors.infrastructure.exception.auth;

import io.jsonwebtoken.JwtException;

public class JwtExceptionWrapper extends JwtException {
    private final static String message = "유효하지 않은 토큰입니다.";

    public JwtExceptionWrapper() {
        super(message);
    }
}
