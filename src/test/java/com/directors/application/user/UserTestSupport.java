package com.directors.application.user;

import com.directors.IntegrationTestSupport;
import com.directors.domain.auth.TokenRepository;
import com.directors.domain.user.UserRepository;
import com.directors.infrastructure.auth.JwtAuthenticationManager;
import com.directors.infrastructure.auth.JwtTokenGenerator;
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
}
