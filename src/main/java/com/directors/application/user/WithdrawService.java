package com.directors.application.user;

import com.directors.domain.auth.TokenRepository;
import com.directors.domain.user.PasswordManager;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.infrastructure.exception.user.AuthenticationFailedException;
import com.directors.infrastructure.exception.user.NoSuchUserException;
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
        String userId = withdrawRequest.userId();
        String password = withdrawRequest.password();

        validateUserIds(userId, userIdByToken);

        User user = userRepository.findJoinedUserById(userId);

        if (user == null) {
            throw new NoSuchUserException(userId);
        }

        if (!pm.checkPassword(password, user.getPassword())) {
            throw new AuthenticationFailedException(user.getUserId());
        }

        user.setWithdrawalDate(new Date());
        user.setStatus(UserStatus.WITHDRAWN);

        userRepository.saveUser(user);

        tokenRepository.deleteAllTokenByUserId(user.getUserId());
    }

    private static void validateUserIds(String firstUserId, String secondUserId) {
        if (!firstUserId.equals(secondUserId)) {
            throw new AuthenticationFailedException(firstUserId);
        }
    }

}
