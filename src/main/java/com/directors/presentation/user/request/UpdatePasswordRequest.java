package com.directors.presentation.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdatePasswordRequest(
        @NotBlank(message = "기존 패스워드가 입력되지 않았습니다.")
        @Size(min = 8, max = 20, message = "기존 패스워드의 길이가 8-20글자 사이로 입력되지 않았습니다.")
        String oldPassword,
        @NotBlank(message = "새 패스워드가 입력되지 않았습니다.")
        @Size(min = 8, max = 20, message = "새 패스워드의 길이가 8-20글자 사이로 입력되지 않았습니다.")
        String newPassword
) {
}
