package com.directors.presentation.user;

import com.directors.application.user.*;
import com.directors.presentation.user.request.*;
import com.directors.presentation.user.response.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final SignUpService signUpService;
    private final AuthenticationService authenticationService;
    private final WithdrawService withdrawService;
    private final UpdateUserService updateUserService;
    private final AuthenticateRegionService authenticateRegionService;
    private final SearchDirectorService searchDiretorService;

    @PostMapping("/signUp")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return new ResponseEntity<>(signUpService.signUp(signUpRequest), HttpStatus.CREATED);
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
    public ResponseEntity<HttpStatus> logOut(@Valid @RequestBody LogOutRequest logOutRequest) {
        authenticationService.logOut(logOutRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/refreshAuthentication")
    public ResponseEntity<RefreshAuthenticationResponse> refreshAuthentication(@Valid @RequestBody RefreshAuthenticationRequest request) {
        return new ResponseEntity<>(authenticationService.refreshAuthentication(request), HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawResponse> withdraw(@Valid @RequestBody WithdrawRequest withdrawRequest, @AuthenticationPrincipal String userIdByToken) {
        return new ResponseEntity<>(withdrawService.withdraw(withdrawRequest, userIdByToken), HttpStatus.OK);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<HttpStatus> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest, @AuthenticationPrincipal String userIdByToken) {
        updateUserService.updatePassword(updatePasswordRequest, userIdByToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO: 03.28 추후 이메일 인증 로직 추가. 현재는 인증 없이 변경 가능.
    @PutMapping("/updateEmail")
    public ResponseEntity<HttpStatus> updateEmail(@Valid @RequestBody UpdateEmailRequest updateEmailRequest, @AuthenticationPrincipal String userIdByToken) {
        updateUserService.updateEmail(updateEmailRequest, userIdByToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/authenticateRegion")
    public ResponseEntity<AuthenticateRegionResponse> authenticateRegion(@RequestBody AuthenticateRegionRequest request, @AuthenticationPrincipal String userIdByToken) {
        var response = authenticateRegionService.authenticate(request, userIdByToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/director/{directorId}")
    public ResponseEntity<GetDirectorResponse> getDirector(
            @PathVariable @NotBlank(message = "입력 값이 존재하지않습니다.")
            @Size(min = 8, max = 20, message = "아이디의 길이가 8-20글자 사이로 입력되지 않았습니다.") String directorId
    ) {
        var director = searchDiretorService.getDirector(directorId);
        return new ResponseEntity<>(director, HttpStatus.OK);
    }

    @PostMapping("/director/list")
    public ResponseEntity<List<SearchDirectorResponse>> searchDirector(@Valid @RequestBody SearchDirectorRequest request, @AuthenticationPrincipal String userIdByToken) {
        var responsesByPaging = searchDiretorService.searchDirector(request, userIdByToken);
        return new ResponseEntity<>(responsesByPaging, HttpStatus.OK);
    }
}
