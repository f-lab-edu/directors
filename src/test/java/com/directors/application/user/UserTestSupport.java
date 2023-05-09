package com.directors.application.user;

import com.directors.IntegrationTestSupport;
import com.directors.domain.auth.TokenRepository;
import com.directors.domain.user.UserRepository;
import com.directors.infrastructure.auth.JwtAuthenticationManager;
import com.directors.infrastructure.auth.JwtTokenGenerator;
import com.directors.presentation.user.request.LogInRequest;
import com.directors.presentation.user.request.SignUpRequest;
import com.directors.presentation.user.request.WithdrawRequest;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class UserTestSupport extends IntegrationTestSupport {
    @Autowired
    public AuthenticationService authenticationService;

    @Autowired
    public SignUpService signUpService;

    @Autowired
    public JwtAuthenticationManager jwtAuthenticationManager;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public TokenRepository tokenRepository;

    @Autowired
    public JwtTokenGenerator tokenGenerator;

    @Autowired
    public UpdateUserService updateUserService;

    @Autowired
    public WithdrawService withdrawService;

    @Autowired
    public AuthenticateRegionService authenticateRegionService;

    protected static LogInRequest createLogInRequest(String userId, String password) {
        return LogInRequest.builder()
                .userId(userId)
                .password(password)
                .build();
    }

    protected static SignUpRequest createSignUpRequest(String userId, String password, String name, String nickname, String email, String phoneNumber) {
        return SignUpRequest.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .nickname(nickname)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
    }

    protected static WithdrawRequest createWithdrawRequest(String userId, String password) {
        return WithdrawRequest.builder()
                .userId(userId)
                .password(password)
                .build();
    }
}
