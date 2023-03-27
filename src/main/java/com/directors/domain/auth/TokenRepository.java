package com.directors.domain.auth;

public interface TokenRepository {
    Token findTokenByTokenString(String tokenString);

    void saveToken(Token token);

    void deleteToken(String tokenString);

    void deleteAllTokenByUserId(String userId);
}
