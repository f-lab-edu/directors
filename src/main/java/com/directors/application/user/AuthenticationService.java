package com.directors.application.user;

import com.directors.domain.auth.Token;
import com.directors.domain.auth.TokenRepository;
import com.directors.domain.user.PasswordManager;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.infrastructure.auth.JwtAuthenticationManager;
import com.directors.infrastructure.exception.user.AuthenticationFailedException;
import com.directors.infrastructure.exception.user.NoSuchUserException;
import com.directors.presentation.user.request.LogInRequest;
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
            throw new NoSuchUserException(userId);
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
    public RefreshAuthenticationResponse refreshAuthentication(RefreshAuthenticationRequest request) {
        String accessToken = request.accessToken();
        String refreshToken = request.refreshToken();

        // 토큰 유효성 검사 1 - 액세스 토큰과 리프레시 토큰의 userId 비교
        String userId = jm.getUserIdByToken(accessToken);
        if (!userId.equals(jm.getUserIdByToken(refreshToken))) {
            throw new JwtException("유효하지 않은 토큰입니다.");
        }

        // 토큰 유효성 검사 2 - 토큰의 소유 user가 존재하는지 여부 검증
        User userById = userRepository.findUserById(userId);
        if (userById == null) {
            throw new JwtException("유효하지 않은 토큰입니다.");
        }

        // 토큰 유효성 검사 3 - 레포지토리를 통한 리프레시 토큰 유효성 검증
        Token tokenByTokenString = tokenRepository.findTokenByTokenString(refreshToken);
        if (tokenByTokenString == null) {
            throw new JwtException("유효하지 않은 토큰입니다.");
        }

        // 토큰 유효성 검사 4 - 유효기간을 통한 리프레시 토큰 유효성 검증
        long refreshExpirationDay = jm.getExpirationDayByToken(refreshToken);
        if (refreshExpirationDay < 0) {
            throw new JwtException("유효하지 않은 토큰입니다.");
        }

        // 액세스 토큰 발급
        accessToken = jm.generateAccessToken(userId);

        // 유효 기간이 일주일 보다 적게 남았을 경우 리프레시 토큰 재발급
        if (refreshExpirationDay < 7) {
            tokenRepository.deleteToken(refreshToken);
            
            refreshToken = jm.generateRefreshToken(userId);
            Date expiration = jm.getExpirationByToken(refreshToken);

            tokenRepository.saveToken(new Token(refreshToken, userId, expiration));
        }

        return new RefreshAuthenticationResponse(accessToken, refreshToken);
    }

}
