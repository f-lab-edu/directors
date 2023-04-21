package com.directors.presentation.user.request;

import com.directors.domain.user.User;
import com.directors.domain.user.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = "아이디가 입력되지 않았습니다.")
        @Size(min = 8, max = 20, message = "아이디의 길이가 8-20글자 사이로 입력되지 않았습니다.")
        String userId,
        @NotBlank(message = "패스워드가 입력되지 않았습니다.")
        @Size(min = 8, max = 20, message = "패스워드의 길이가 8-20글자 사이로 입력되지 않았습니다.")
        String password,
        @NotBlank(message = "이름이 입력되지 않았습니다.")
        @Pattern(regexp = "^[가-힣 ]+$", message = "올바른 이름의 형식이 아닙니다.")
        @Size(min = 2, max = 6, message = "이름의 길이가 2-6글자 사이로 입력되지 않았습니다.")
        String name,
        @NotBlank(message = "닉네임이 입력되지 않았습니다.")
        @Size(min = 8, max = 20, message = "닉네임의 길이가 8-20글자 사이로 입력되지 않았습니다.")
        String nickname,
        @NotBlank(message = "이메일이 입력되지 않았습니다.")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "올바른 이메일 형식이 아닙니다.")
        String email,
        @NotBlank(message = "전화번호가 입력되지 않았습니다.")
        @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", message = "올바른 휴대전화 형식이 아닙니다.")
        String phoneNumber
) {
    public User toEntity() {
        // region은 회원 가입 후, 추가로 설정할 수 있는 로직을 만들 예정입니다.
        return User.builder()
                .id(userId)
                .password(password)
                .name(name)
                .nickname(nickname)
                .email(email)
                .phoneNumber(phoneNumber)
                .userStatus(UserStatus.JOINED)
                .build();
    }
}