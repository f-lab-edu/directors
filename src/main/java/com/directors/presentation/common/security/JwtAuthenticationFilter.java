package com.directors.presentation.common.security;

import com.directors.infrastructure.auth.JwtAuthenticationManager;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtAuthenticationManager jm;

    private final List<String> excludedPaths = List.of("/user/logIn", "/user/signUp", "/user/refreshAuthentication", "/chat");

    private final Pattern duplicatedPattern = Pattern.compile("/user/duplicated/.*");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        if (excludedPaths.contains(path) || duplicatedPattern.matcher(path).matches()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 유무 여부 확인
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            generateTokenExceptionMessage(response, "인증을 위해 JWT 토큰이 필요합니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 데이터 검증
        String token = authHeader.substring("Bearer ".length());

        try {
            var authentication = jm.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), null, authentication.getAuthorities()));
        } catch (ExpiredJwtException e) {
            generateTokenExceptionMessage(response, "토큰 기한이 만료되었습니다.");
        } catch (JwtException | UsernameNotFoundException e) {
            generateTokenExceptionMessage(response, "잘못된 토큰 형식입니다.");
        }

        filterChain.doFilter(request, response);
    }

    private static void generateTokenExceptionMessage(HttpServletResponse response, String message) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.getWriter().println("{ \"message\": \"" + message + "\" }");
    }
}
