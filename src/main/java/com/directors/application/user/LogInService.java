package com.directors.application.user;

import com.directors.domain.user.PasswordManager;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.infrastructure.auth.JwtAuthenticationManager;
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
    private final PasswordManager pm;
    private final JwtAuthenticationManager jm;

    @Transactional
    public String logIn(LogInRequest loginRequest) {
        String userId = loginRequest.userId();
        String password = loginRequest.password();

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new NoSuchUserException(userId);
        }

        if (!pm.checkPassword(password, user.getPassword())) {
            throw new AuthenticationFailedException(user.getUserId());
        }

        return jm.generateJwtToken(userId);
    }
}
