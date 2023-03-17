package com.directors.presentation.user;

import com.directors.presentation.user.request.SignUpRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{id}/duplicated")
    public ResponseEntity<HttpStatus> idCheck(
            @PathVariable @NotBlank(message = "입력 값이 존재하지않습니다.")
            @Size(min = 8, max = 20, message = "아이디의 길이가 8-20글자 사이로 입력되지 않았습니다.") String id
    ) {
        log.info("요청된 id: " + id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
