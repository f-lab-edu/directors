package com.directors.application.user;

import com.directors.domain.auth.TokenRepository;
import com.directors.domain.user.PasswordManager;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.domain.user.exception.AuthenticationFailedException;
import com.directors.presentation.user.request.WithdrawRequest;
import com.directors.presentation.user.response.WithdrawResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final UserRepository userRepository;
    private final PasswordManager pm;
    private final TokenRepository tokenRepository;

    @Transactional
    public WithdrawResponse withdraw(WithdrawRequest withdrawRequest, String userIdByToken) {
        var userId = withdrawRequest.userId();
        var password = withdrawRequest.password();

        validateUserIds(userId, userIdByToken);

        var user = userRepository
                .findByIdAndUserStatus(userId, UserStatus.JOINED)
                .filter(u -> pm.checkPassword(password, u.getPassword()))
                .orElseThrow(() -> new AuthenticationFailedException(userId));

        user.withdrawal(LocalDateTime.now());

        tokenRepository.deleteAllTokenByUserId(user.getId());

        return new WithdrawResponse(userId);
    }

    private void validateUserIds(String firstUserId, String secondUserId) {
        if (!firstUserId.equals(secondUserId)) {
            throw new AuthenticationFailedException(firstUserId);
        }
    }
}
