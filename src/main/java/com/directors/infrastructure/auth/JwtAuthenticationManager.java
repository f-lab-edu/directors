package com.directors.infrastructure.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtAuthenticationManager {

    @Value("${jwtSecretKey}")
    private String secretKey;

    public Authentication getAuthentication(String token) {
        String userId = getBodyByToken(token).getSubject();
        return userId != null ? new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList()) : null;
    }

    public Optional<String> getUserIdByToken(String token) {
        return Optional.ofNullable(getBodyByToken(token).getSubject());
    }

    public long getExpirationDayByToken(String token) {
        long now = new Date(System.currentTimeMillis()).getTime();
        long expiration = getExpirationByToken(token).getTime();

        return TimeUnit.DAYS.convert((expiration - now), TimeUnit.MILLISECONDS);
    }

    public Date getExpirationByToken(String token) {
        return getBodyByToken(token).getExpiration();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private Claims getBodyByToken(String token) throws JwtException {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
