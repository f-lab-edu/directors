package com.directors.infrastructure.api;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ApiResponseValidator {
    private static final String CertificationErrorString = "인증 정보가 존재하지 않습니다";
    private static final String NotFoundErrorString = "검색결과가 존재하지 않습니다.";

    public static boolean isCertificated(ResponseEntity<Map<String, Object>> response) {
        if (response.getBody().get("errMsg").equals(CertificationErrorString)) {
            return false;
        }
        return true;
    }

    public static void checkNotFound(ResponseEntity<Map<String, Object>> response) {
        if (response.getBody().get("errMsg").equals(NotFoundErrorString)) {
            throw new IllegalArgumentException();
        }
    }
}
