package com.directors.presentation.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateEmailRequest(
        @NotBlank(message = "새 이메일이 입력되지 않았습니다.")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "올바른 이메일 형식이 아닙니다.")
        String newEmail
) {
}
