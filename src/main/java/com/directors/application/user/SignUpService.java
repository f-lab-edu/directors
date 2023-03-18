package com.directors.application.user;

import com.directors.domain.user.PasswordManager;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.infrastructure.exception.user.DuplicateIdException;
import com.directors.presentation.user.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final PasswordManager pm;

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        isDuplicatedUser(signUpRequest.userId());

        User newUser = User.builder()
                .userId(signUpRequest.userId())
                .password(pm.encodePassword(signUpRequest.password()))
                .name(signUpRequest.name())
                .nickname(signUpRequest.nickname())
                .email(signUpRequest.email())
                .phoneNumber(signUpRequest.phoneNumber())
                .build();

        userRepository.saveUser(newUser);
    }

    @Transactional
    public void isDuplicatedUser(String id) {
        if (userRepository.findUserById(id) != null) {
            throw new DuplicateIdException("이미 존재하는 Id입니다");
        }
    }
}
