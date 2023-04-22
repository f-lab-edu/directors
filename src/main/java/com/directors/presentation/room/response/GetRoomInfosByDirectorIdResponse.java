package com.directors.presentation.room.response;

import java.time.LocalDateTime;

public record GetRoomInfosByDirectorIdResponse(
        Long roomId,
        Long questionId,
        String questionerId,
        String recentChatContent,
        LocalDateTime recentChatTime
) {
}
