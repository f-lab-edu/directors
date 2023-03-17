package com.directors.presentation.user;

import com.directors.presentation.user.request.SignUpRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @PostMapping("/signUp")
    public ResponseEntity<HttpStatus> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.info(signUpRequest.toString());
        log.info("회원 가입 완료");

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
