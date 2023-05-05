package com.directors.application.user;

import com.directors.domain.auth.Token;
import com.directors.domain.auth.TokenRepository;
import com.directors.domain.user.PasswordManager;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.domain.user.exception.AuthenticationFailedException;
import com.directors.infrastructure.auth.JwtAuthenticationManager;
import com.directors.infrastructure.auth.JwtTokenGenerator;
import com.directors.presentation.user.request.LogInRequest;
import com.directors.presentation.user.request.LogOutRequest;
import com.directors.presentation.user.request.RefreshAuthenticationRequest;
import com.directors.presentation.user.response.LogInResponse;
import com.directors.presentation.user.response.RefreshAuthenticationResponse;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordManager passwordManager;
    private final JwtAuthenticationManager authenticationManager;
    private final JwtTokenGenerator tokenGenerator;

    @Transactional
    public LogInResponse logIn(LogInRequest loginRequest) {
        var userId = loginRequest.userId();
        var password = loginRequest.password();

        User user = validateUser(userId, password);

        var jwtToken = tokenGenerator.generateAccessToken(user.getId());
        var refreshToken = tokenGenerator.generateRefreshToken(user.getId());
        var refreshTokenExpiration = authenticationManager.getExpirationByToken(refreshToken);

        tokenRepository.saveToken(new Token(refreshToken, user.getId(), refreshTokenExpiration));

        return new LogInResponse(jwtToken, refreshToken);
    }

    @Transactional
    public void logOut(LogOutRequest logOutRequest) {
        String refreshToken = logOutRequest.refreshToken();

        validateTokenWithExpireDay(refreshToken);

        tokenRepository.deleteToken(logOutRequest.refreshToken());
    }

    @Transactional
    public RefreshAuthenticationResponse refreshAuthentication(RefreshAuthenticationRequest request) {
        var accessToken = request.accessToken();
        var refreshToken = request.refreshToken();

        var userId = compareUserIdWithTokens(accessToken, refreshToken);

        validateUserIdByToken(userId);

        accessToken = tokenGenerator.generateAccessToken(userId);

        long refreshExpirationDay = validateTokenWithExpireDay(refreshToken);
        refreshToken = refreshTokenIfExpiringWithinWeek(refreshToken, userId, refreshExpirationDay);

        return new RefreshAuthenticationResponse(accessToken, refreshToken);
    }

    private User validateUser(String userId, String password) {
        var user = userRepository
                .findByIdAndUserStatus(userId, UserStatus.JOINED)
                .filter(u -> passwordManager.checkPassword(password, u.getPassword()))
                .orElseThrow(() -> new AuthenticationFailedException(userId));
        return user;
    }

    private String compareUserIdWithTokens(String accessToken, String refreshToken) {
        String userIdByAccessToken = authenticationManager
                .getUserIdByToken(accessToken)
                .orElseThrow(() -> new JwtException("유효하지 않은 토큰입니다."));
        String userIdByRefreshToken = authenticationManager
                .getUserIdByToken(refreshToken)
                .orElseThrow(() -> new JwtException("유효하지 않은 토큰입니다."));

        if (!userIdByAccessToken.equals(userIdByRefreshToken)) {
            throw new JwtException("유효하지 않은 토큰입니다.");
        }

        return userIdByAccessToken;
    }

    private void validateUserIdByToken(String userId) {
        userRepository
                .findByIdAndUserStatus(userId, UserStatus.JOINED)
                .orElseThrow(() -> new JwtException("유효하지 않은 토큰입니다."));
    }

    private long validateTokenWithExpireDay(String token) {
        tokenRepository
                .findTokenByTokenString(token)
                .orElseThrow(() -> new JwtException("유효하지 않은 토큰입니다."));

        long refreshExpirationDay = authenticationManager.getExpirationDayByToken(token);

        return refreshExpirationDay;
    }

    private String refreshTokenIfExpiringWithinWeek(String refreshToken, String userId, long expirationDay) {
        if (expirationDay < 7) {
            tokenRepository.deleteToken(refreshToken);

            refreshToken = tokenGenerator.generateRefreshToken(userId);
            Date expiration = authenticationManager.getExpirationByToken(refreshToken);

            tokenRepository.saveToken(new Token(refreshToken, userId, expiration));
        }
        return refreshToken;
    }
}
