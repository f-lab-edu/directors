package com.directors.infrastructure.config;

import com.directors.infrastructure.auth.JwtAuthenticationManager;
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

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthenticationManager jm;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().
                authorizeHttpRequests()
                .requestMatchers(
                        new AntPathRequestMatcher("/user/signUp", "POST"),
                        new AntPathRequestMatcher("/user/logIn", "POST")
                ).permitAll()
                .anyRequest().authenticated().and()
                .addFilterBefore(new JwtAuthenticationFilter(jm), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}
