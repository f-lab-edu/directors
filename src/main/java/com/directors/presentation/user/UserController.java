package com.directors.presentation.user;

import com.directors.application.user.LogInService;
import com.directors.application.user.SignUpService;
import com.directors.presentation.user.request.LogInRequest;
import com.directors.presentation.user.request.SignUpRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final SignUpService signUpService;
    private final LogInService logInService;

    @PostMapping("/signUp")
    public ResponseEntity<HttpStatus> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        signUpService.signUp(signUpRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/duplicated/{id}")
    public ResponseEntity<HttpStatus> isDuplicateId(
            @PathVariable @NotBlank(message = "입력 값이 존재하지않습니다.")
            @Size(min = 8, max = 20, message = "아이디의 길이가 8-20글자 사이로 입력되지 않았습니다.") String id
    ) {
        signUpService.isDuplicatedUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logIn")
    public ResponseEntity<String> logIn(@Valid @RequestBody LogInRequest loginRequest) {
        String tokenData = logInService.logIn(loginRequest);

        // TODO: 2023/03/19 jwt 발급 로직 추가 후 응답 로직 수정
        
        return new ResponseEntity<>(tokenData, HttpStatus.OK);
    }
}
