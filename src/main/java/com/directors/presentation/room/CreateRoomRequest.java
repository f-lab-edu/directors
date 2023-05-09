package com.directors.presentation.room;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@Builder
public record CreateRoomRequest(
        @PathVariable @NotBlank(message = "입력 값이 존재하지않습니다.")
        Long questionId,
        @NotBlank(message = "입력 값이 존재하지않습니다.")
        LocalDateTime requestTime
) {
}
