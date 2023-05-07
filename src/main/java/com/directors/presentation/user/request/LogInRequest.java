package com.directors.presentation.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LogInRequest(
        @NotBlank(message = "아이디가 입력되지 않았습니다.")
        String userId,
        @NotBlank(message = "패스워드가 입력되지 않았습니다.")
        String password
) {
}
