package com.directors.presentation.room.response;

import com.directors.domain.chat.Chat;
import com.directors.domain.room.Room;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetRoomInfosByQuestionerIdResponse(
        Long roomId,
        Long questionId,
        String directorId,
        String recentChatContent,
        LocalDateTime recentChatTime
) {
    public static GetRoomInfosByQuestionerIdResponse from(Room room, Chat chat) {
        return GetRoomInfosByQuestionerIdResponse.builder()
                .roomId(room.getId())
                .questionId(room.getQuestion().getId())
                .directorId(room.getDirector().getId())
                .recentChatContent(chat.getContent())
                .recentChatTime(chat.getSendTime())
                .build();
    }
}
