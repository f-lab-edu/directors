package com.directors.infrastructure.auth;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

@Slf4j
@Component
public class JwtAuthenticationManager {

    @Value("${jwtSecretKey}")
    private String secretKey;

    public String generateJwtToken(String userId) {
        Date now = new Date();

        String compact = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("directors.com")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofDays(1).toMillis())) // 유효 기간: 1일
                .setSubject(userId)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
        return compact;
    }

    public Authentication getAuthentication(String token) {
        String userId = Jwts
                .parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();

        return userId != null ? new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList()) : null;
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
