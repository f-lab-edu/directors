package com.directors.application.user;

import com.directors.domain.auth.TokenRepository;
import com.directors.domain.user.PasswordManager;
import com.directors.domain.user.User;
import com.directors.domain.user.UserStatus;
import com.directors.infrastructure.exception.user.AuthenticationFailedException;
import com.directors.infrastructure.jpa.user.JpaUserRepository;
import com.directors.presentation.user.request.WithdrawRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final JpaUserRepository userRepository;
    private final PasswordManager pm;
    private final TokenRepository tokenRepository;

    @Transactional
    public void withdraw(WithdrawRequest withdrawRequest, String userIdByToken) {
        String userId = withdrawRequest.userId();
        String password = withdrawRequest.password();

        validateUserIds(userId, userIdByToken);

        var user = userRepository.findByIdAndUserStatus(userId, UserStatus.JOINED);

        User loadedUser = user
                .filter(u -> pm.checkPassword(password, u.getPassword()))
                .orElseThrow(() -> new AuthenticationFailedException(userId));

        loadedUser.withdrawal(new Date());

        userRepository.save(loadedUser);

        tokenRepository.deleteAllTokenByUserId(loadedUser.getId());
    }

    private static void validateUserIds(String firstUserId, String secondUserId) {
        if (!firstUserId.equals(secondUserId)) {
            throw new AuthenticationFailedException(firstUserId);
        }
    }

}
