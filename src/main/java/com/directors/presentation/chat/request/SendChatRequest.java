package com.directors.presentation.chat.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
public record SendChatRequest(
        @PositiveOrZero(message = "잘못된 요청입니다.") Long roomId,
        @NotBlank(message = "채팅이 입력되지 않았습니다.") String chatContent,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        @NotNull(message = "보낸 시간이 입력되지 않았습니다.")
        LocalDateTime sendTime

) {
}
