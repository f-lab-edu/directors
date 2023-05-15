package com.directors.presentation.room;

import jakarta.validation.constraints.*;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
public record CreateRoomRequest(
        @Positive(message = "입력 값이 존재하지 않습니다.")
        Long questionId,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        @NotNull(message = "요청 시간이 입력되지 않았습니다.")
        LocalDateTime requestTime
) {
}
