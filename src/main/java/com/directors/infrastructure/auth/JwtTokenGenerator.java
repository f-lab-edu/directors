package com.directors.infrastructure.auth;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtTokenGenerator {

    @Value("${jwtSecretKey}")
    private String secretKey;

    public String generateAccessToken(String userId) {
        return generateJwtTokenWithDay(userId, 1);
    }

    public String generateRefreshToken(String userId) {
        return generateJwtTokenWithDay(userId, 30);
    }

    public String generateJwtTokenWithDay(String userId, int day) throws JwtException {
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

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
