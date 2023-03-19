package com.directors.application.user;

import com.directors.domain.user.PasswordManager;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.infrastructure.exception.user.AuthenticationFailedException;
import com.directors.infrastructure.exception.user.NoSuchUserException;
import com.directors.presentation.user.request.LogInRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogInService {

    private final UserRepository userRepository;
    private final PasswordManager passwordManager;

    @Transactional
    public String logIn(LogInRequest loginRequest) {
        String userId = loginRequest.userId();
        String password = loginRequest.password();

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new NoSuchUserException(userId);
        }

        if (!passwordManager.checkPassword(password, user.getPassword())) {
            throw new AuthenticationFailedException(user.getUserId());
        }

        // TODO: 2023/03/19 jwt 발급 로직 추가 필요

        return "LoginSuccessToken";
    }
}
