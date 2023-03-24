package com.directors.presentation.common.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * API 요청에 대한 JwtAuthenticationFilter에서의 토큰 검증이 실패할 경우,
 * 스프링 시큐리티 컨텍스트에서 사용할 Authentication를 획득하지 못하므로 아래의 commence 메서드를 통해 요청이 처리됩니다.
 * 만약 유저의 ROLE(USER, ADMIN..)을 설정한다면 다른 처리가 필요할 수 있지만,
 * 현재는 USER ROLE만 있으므로 스프링 시큐리티에서 Authentication를 소유하고 있는지 여부만 판별하여 유저 인증 절차가 처리되도록 했습니다.
 */
// TODO: 유지 여부 검토 필요
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("AuthenticationException occurred.");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
    }
}