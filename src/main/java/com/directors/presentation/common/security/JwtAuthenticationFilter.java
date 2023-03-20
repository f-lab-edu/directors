package com.directors.presentation.common.security;

import com.directors.domain.AuthenticationManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager am;
    private final List<String> excludedPaths = List.of("/user/logIn", "/user/signUp");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (excludedPaths.contains(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 인증 데이터입니다.");
            return;
        }

        String token = authHeader.substring("Bearer ".length());

        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(am.getSecretKey().getBytes())
                    .build()
                    .parseClaimsJws(token);

            httpRequest.setAttribute("user", claimsJws.getBody().getSubject());

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증이 실패했습니다.");
        }
    }
}
