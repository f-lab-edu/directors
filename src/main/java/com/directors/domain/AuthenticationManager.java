package com.directors.domain;

public interface AuthenticationManager {
    String generateAuthenticationToken(String userId);
}
