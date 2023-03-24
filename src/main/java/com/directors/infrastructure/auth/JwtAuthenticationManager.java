package com.directors.infrastructure.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtAuthenticationManager {

    @Value("${jwtSecretKey}")
    private String secretKey;

    public String generateAccessToken(String userId) {
        return generateJwtTokenWithDay(userId, 0);
    }

    public String generateRefreshToken(String userId) {
        return generateJwtTokenWithDay(userId, 30);
    }

    public Authentication getAuthentication(String token) {
        String userId = getBodyByToken(token).getSubject();
        return userId != null ? new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList()) : null;
    }

    public String getUserIdByToken(String token) {
        return getBodyByToken(token).getSubject();
    }

    public long getExpirationDayByToken(String token) {
        long now = new Date(System.currentTimeMillis()).getTime();
        long expiration = getExpirationByToken(token).getTime();

        return TimeUnit.DAYS.convert((expiration - now), TimeUnit.MILLISECONDS);
    }

    public Date getExpirationByToken(String token) {
        return getBodyByToken(token).getExpiration();
    }

    private Key getSecretKey() throws JwtException {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private String generateJwtTokenWithDay(String userId, int day) throws JwtException {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("directors.com")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofDays(day).toMillis()))
                .setSubject(userId)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
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
