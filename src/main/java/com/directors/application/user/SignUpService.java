package com.directors.application.user;

import com.directors.domain.user.PasswordManager;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.infrastructure.exception.user.DuplicateIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final PasswordManager pm;

    @Transactional
    public void signUp(User newUser) {
        isDuplicatedUser(newUser.getUserId());

        newUser.setPasswordByEncryption(pm.encryptPassword(newUser.getPassword()));

        userRepository.saveUser(newUser);
    }

    @Transactional
    public void isDuplicatedUser(String id) {
        if (userRepository.findUserByIdAndUserStatus(id, UserStatus.JOINED) != null) {
            throw new DuplicateIdException(id);
        }
    }
}
