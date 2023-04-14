package com.directors.presentation.room.response;

import java.time.LocalDateTime;

public record GetRoomInfosByQuestionerIdResponse(
        Long roomId,
        Long questionId,
        String directionerId,
        String recentChatContent,
        LocalDateTime recentChatTime
) {
}
