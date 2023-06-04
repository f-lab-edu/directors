package com.directors.presentation.chat.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendChatRequest(
        @NotNull(message = "잘못된 요청입니다.") Long roomId,
        @NotBlank(message = "채팅이 입력되지 않았습니다.") String chatContent
) {
}
