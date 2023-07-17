package com.directors.infrastructure.config;

import com.directors.presentation.common.security.JwtAuthenticationEntryPoint;
import com.directors.presentation.common.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthenticationEntryPoint je;
    private final JwtAuthenticationFilter jf;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().
                authorizeHttpRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .requestMatchers(
                        new AntPathRequestMatcher("/user/signUp", "POST"),
                        new AntPathRequestMatcher("/user/logIn", "POST"),
                        new AntPathRequestMatcher("/user/refreshAuthentication", "POST")
                ).permitAll()
                .anyRequest().authenticated().and() // 인증 정보가 없을 경우 JwtAuthenticationEntryPoint.commerce() 메서드로 처리
                .addFilterBefore(jf, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(je).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}
