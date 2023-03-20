package com.directors.infrastructure.auth;

import com.directors.domain.AuthenticationManager;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtAuthenticationManager implements AuthenticationManager {

    @Value("${jwtSecretKey}")
    private String secretKey;

    @Override
    public String generateAuthenticationToken(String userId) {
        Date now = new Date();
        Key signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("directors.com") // 토큰 발급자
                .setIssuedAt(now) // 발급 시간(iat)
                .setExpiration(new Date(now.getTime() + Duration.ofDays(7).toMillis())) // 만료 시간(exp)
                .setSubject("userAuth")
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }
}
