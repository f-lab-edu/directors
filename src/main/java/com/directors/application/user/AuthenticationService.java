package com.directors.application.user;

import com.directors.domain.auth.Token;
import com.directors.domain.auth.TokenRepository;
import com.directors.domain.user.PasswordManager;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.infrastructure.auth.JwtAuthenticationManager;
import com.directors.infrastructure.exception.user.AuthenticationFailedException;
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
    private final PasswordManager pm;
    private final JwtAuthenticationManager jm;

    @Transactional
    public LogInResponse logIn(LogInRequest loginRequest) {
        String userId = loginRequest.userId();
        String password = loginRequest.password();

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new AuthenticationFailedException(userId);
        }

        if (!pm.checkPassword(password, user.getPassword())) {
            throw new AuthenticationFailedException(user.getUserId());
        }

        String jwtToken = jm.generateAccessToken(userId);
        String refreshToken = jm.generateRefreshToken(userId);
        Date refreshTokenExpiration = jm.getExpirationByToken(refreshToken);

        tokenRepository.saveToken(new Token(refreshToken, userId, refreshTokenExpiration));

        return new LogInResponse(jwtToken, refreshToken);
    }

    @Transactional
    public void logOut(LogOutRequest logOutRequest) {
        tokenRepository.deleteToken(logOutRequest.refreshToken());
    }

    @Transactional
    public RefreshAuthenticationResponse refreshAuthentication(RefreshAuthenticationRequest request) {
        String accessToken = request.accessToken();
        String refreshToken = request.refreshToken();

        String userId = compareUserIdWithTokens(accessToken, refreshToken);
        validateUserIdByToken(userId);

        accessToken = jm.generateAccessToken(userId);

        long refreshExpirationDay = validateRefreshToken(refreshToken);
        refreshToken = refreshTokenIfExpiringWithinWeek(refreshToken, userId, refreshExpirationDay);

        return new RefreshAuthenticationResponse(accessToken, refreshToken);
    }

    private String compareUserIdWithTokens(String accessToken, String refreshToken) {
        String userIdByAccessToken = jm.getUserIdByToken(accessToken);
        String userIdByRefreshToken = jm.getUserIdByToken(refreshToken);

        if (userIdByAccessToken == null || userIdByRefreshToken == null || !userIdByAccessToken.equals(userIdByRefreshToken)) {
            throw new JwtException("유효하지 않은 토큰입니다.");
        }

        return userIdByAccessToken;
    }

    private void validateUserIdByToken(String userIdByToken) {
        if (userRepository.findUserById(userIdByToken) == null) {
            throw new JwtException("유효하지 않은 토큰입니다.");
        }
    }

    private long validateRefreshToken(String refreshToken) {
        Token tokenByTokenString = tokenRepository.findTokenByTokenString(refreshToken);
        if (tokenByTokenString == null) {
            throw new JwtException("유효하지 않은 토큰입니다.");
        }

        long refreshExpirationDay = jm.getExpirationDayByToken(refreshToken);
        if (refreshExpirationDay < 0) {
            throw new JwtException("유효하지 않은 토큰입니다.");
        }

        return refreshExpirationDay;
    }

    private String refreshTokenIfExpiringWithinWeek(String refreshToken, String userId, long expirationDay) {
        if (expirationDay < 7) {
            tokenRepository.deleteToken(refreshToken);

            refreshToken = jm.generateRefreshToken(userId);
            Date expiration = jm.getExpirationByToken(refreshToken);

            tokenRepository.saveToken(new Token(refreshToken, userId, expiration));
        }
        return refreshToken;
    }
}
