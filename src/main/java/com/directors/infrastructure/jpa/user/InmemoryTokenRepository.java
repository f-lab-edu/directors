package com.directors.infrastructure.jpa.user;

import com.directors.domain.auth.Token;
import com.directors.domain.auth.TokenRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InmemoryTokenRepository implements TokenRepository {
    private final Map<String, Token> tokenMap = new HashMap();

    @Override
    public Optional<Token> findTokenByTokenString(String tokenString) {
        return Optional.ofNullable(tokenMap.get(tokenString));
    }

    @Override
    public void saveToken(Token token) {
        tokenMap.put(token.getTokenString(), token);
    }

    @Override
    public void deleteToken(String tokenString) {
        tokenMap.remove(tokenString);
    }

    @Override
    public void deleteAllTokenByUserId(String userId) {
        tokenMap.entrySet()
                .removeIf(tokenEntry -> tokenEntry.getValue().getUserId().equals(userId));
    }
}
