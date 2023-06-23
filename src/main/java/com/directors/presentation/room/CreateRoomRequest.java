package com.directors.presentation.room;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateRoomRequest(
        @NotNull(message = "입력 값이 존재하지않습니다.")
        Long questionId,
        @NotNull(message = "요청 시간이 입력되지 않았습니다.")
        LocalDateTime requestTime
) {
}
