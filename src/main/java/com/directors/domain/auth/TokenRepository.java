package com.directors.domain.auth;

import java.util.Optional;

public interface TokenRepository {
    Optional<Token> findTokenByTokenString(String tokenString);

    void saveToken(Token token);

    void deleteToken(String tokenString);

    void deleteAllTokenByUserId(String userId);
}
