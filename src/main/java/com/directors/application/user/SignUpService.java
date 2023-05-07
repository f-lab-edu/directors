package com.directors.application.user;

import com.directors.domain.user.PasswordManager;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.exception.DuplicateIdException;
import com.directors.presentation.user.request.SignUpRequest;
import com.directors.presentation.user.response.SignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final PasswordManager pm;

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        User user = signUpRequest.toEntity();

        isDuplicatedUser(user.getId());

        user.setPasswordByEncryption(pm.encryptPassword(user.getPassword()));

        return SignUpResponse.of(userRepository.save(user));
    }

    @Transactional
    public void isDuplicatedUser(String id) {
        var user = userRepository.findById(id);
        user.ifPresent(u -> {
            throw new DuplicateIdException(id);
        });
    }
}
