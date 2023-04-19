package com.directors.application.user;

import com.directors.domain.user.PasswordManager;
import com.directors.domain.user.User;
import com.directors.infrastructure.exception.user.DuplicateIdException;
import com.directors.infrastructure.jpa.user.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final JpaUserRepository userRepository;
    private final PasswordManager pm;

    @Transactional
    public void signUp(User newUser) {
        isDuplicatedUser(newUser.getUserId());

        newUser.setPasswordByEncryption(pm.encryptPassword(newUser.getPassword()));

        userRepository.save(newUser);
    }

    @Transactional
    public void isDuplicatedUser(String id) {
        var user = userRepository.findById(id);
        user.ifPresent(u -> {
            throw new DuplicateIdException(id);
        });
    }
}
