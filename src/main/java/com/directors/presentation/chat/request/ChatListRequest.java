package com.directors.presentation.chat.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record ChatListRequest(
        @PositiveOrZero(message = "요청 Id가 입력되지 않았습니다.") Long roomId,
        @PositiveOrZero(message = "잘못된 요청입니다.") Integer offset,
        @PositiveOrZero(message = "잘못된 요청입니다.") Integer size,
        @NotBlank(message = "요청 Id가 입력되지 않았습니다.") String userId
) {
}
