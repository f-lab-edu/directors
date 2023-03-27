package com.directors.presentation.user;

import com.directors.application.user.AuthenticationService;
import com.directors.application.user.SignUpService;
import com.directors.application.user.WithdrawService;
import com.directors.presentation.user.request.*;
import com.directors.presentation.user.response.LogInResponse;
import com.directors.presentation.user.response.RefreshAuthenticationResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final SignUpService signUpService;
    private final AuthenticationService authenticationService;
    private final WithdrawService withdrawService;

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
    public ResponseEntity<LogInResponse> logIn(@Valid @RequestBody LogInRequest loginRequest) {
        return new ResponseEntity<>(authenticationService.logIn(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/logOut")
    public ResponseEntity<HttpStatus> logOut(@RequestBody LogOutRequest logOutRequest) {
        authenticationService.logOut(logOutRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal String userId) {
        return userId;
    }

    @PostMapping("/refreshAuthentication")
    public ResponseEntity<RefreshAuthenticationResponse> refreshAuthentication(@RequestBody RefreshAuthenticationRequest request) {
        return new ResponseEntity<>(authenticationService.refreshAuthentication(request), HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<HttpStatus> withdraw(@RequestBody WithdrawRequest withdrawRequest, @AuthenticationPrincipal String userIdByToken) {
        withdrawService.withdraw(withdrawRequest, userIdByToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
