package com.directors.infrastructure.config;

import com.directors.infrastructure.auth.JwtAuthenticationManager;
import com.directors.presentation.common.security.JwtAuthenticationEntryPoint;
import com.directors.presentation.common.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthenticationManager jm;
    private final JwtAuthenticationEntryPoint je;
    private final JwtAuthenticationFilter jf;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().
                authorizeHttpRequests()
                .requestMatchers(
                        new AntPathRequestMatcher("/user/signUp", "POST"),
                        new AntPathRequestMatcher("/user/logIn", "POST")
                ).permitAll()
                .anyRequest().authenticated().and() // 인증 정보가 없을 경우 JwtAuthenticationEntryPoint.commerce() 메서드로 처리
                .addFilterBefore(jf, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(je).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public FilterChainProxy filterChainProxy(HttpSecurity http) throws Exception {
        SecurityFilterChain securityFilterChain = new DefaultSecurityFilterChain(
                new AntPathRequestMatcher("/**"), jf);
        return new FilterChainProxy(securityFilterChain);
    }
}
