package com.directors.application.user;

import com.directors.domain.auth.TokenRepository;
import com.directors.domain.user.PasswordManager;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.infrastructure.exception.user.AuthenticationFailedException;
import com.directors.presentation.user.request.WithdrawRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final UserRepository userRepository;
    private final PasswordManager pm;
    private final TokenRepository tokenRepository;

    @Transactional
    public void withdraw(WithdrawRequest withdrawRequest, String userIdByToken) {
        var userId = withdrawRequest.userId();
        var password = withdrawRequest.password();

        validateUserIds(userId, userIdByToken);

        var user = userRepository
                .findByIdAndUserStatus(userId, UserStatus.JOINED)
                .filter(u -> pm.checkPassword(password, u.getPassword()))
                .orElseThrow(() -> new AuthenticationFailedException(userId));

        user.withdrawal(new Date());

        tokenRepository.deleteAllTokenByUserId(user.getId());
    }

    private void validateUserIds(String firstUserId, String secondUserId) {
        if (!firstUserId.equals(secondUserId)) {
            throw new AuthenticationFailedException(firstUserId);
        }
    }
}
