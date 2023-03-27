package com.directors.infrastructure.jpa.user;

import com.directors.domain.auth.Token;
import com.directors.domain.auth.TokenRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
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

    @Override
    public void deleteAllTokenByUserId(String userId) {
        Iterator<Map.Entry<String, Token>> iterator = tokenMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Token> entry = iterator.next();
            Token token = entry.getValue();
            if (token.getUserId().equals(userId)) {
                iterator.remove();
            }
        }
    }
}
