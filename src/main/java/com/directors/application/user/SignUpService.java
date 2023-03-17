package com.directors.application.user;

import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.infrastructure.exception.user.DuplicateIdException;
import com.directors.presentation.user.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;

    // @Transactional 적용 예정
    public void signUp(SignUpRequest signUpRequest) {
        isDuplicatedUser(signUpRequest.userId());

        User newUser = User.builder()
                .userId(signUpRequest.userId())
                .password(signUpRequest.password()) // password 암호화 처리 필요
                .name(signUpRequest.name())
                .nickname(signUpRequest.nickname())
                .email(signUpRequest.email())
                .phoneNumber(signUpRequest.phoneNumber())
                .build();

        userRepository.saveUser(newUser);
    }

    public void isDuplicatedUser(String id) {
        if (userRepository.findUserById(id) != null) {
            throw new DuplicateIdException("이미 존재하는 Id입니다");
        }
    }
}
