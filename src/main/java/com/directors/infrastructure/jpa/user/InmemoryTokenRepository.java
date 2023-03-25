package com.directors.infrastructure.jpa.user;

import com.directors.domain.auth.Token;
import com.directors.domain.auth.TokenRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InmemoryTokenRepository implements TokenRepository {

    private final Map<String, Token> tokenMap = new HashMap();

    @Override
    public Token findTokenByTokenString(String tokenString) {
        return tokenMap.get(tokenString);
    }

    @Override
    public void saveToken(Token token) {
        tokenMap.put(token.getTokenString(), token);
    }

    @Override
    public void deleteToken(String tokenString) {
        tokenMap.remove(tokenString);
    }
}
