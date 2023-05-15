package com.directors.presentation.room.response;

import com.directors.domain.chat.Chat;
import com.directors.domain.room.Room;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetRoomInfosByDirectorIdResponse(
        Long roomId,
        Long questionId,
        String questionerId,
        String recentChatContent,
        LocalDateTime recentChatTime
) {
    public static GetRoomInfosByDirectorIdResponse from(Room room, Chat chat) {
        return GetRoomInfosByDirectorIdResponse.builder()
                .roomId(room.getId())
                .questionId(room.getQuestion().getId())
                .questionerId(room.getQuestioner().getId())
                .recentChatContent(chat.getContent())
                .recentChatTime(chat.getCreatedTime())
                .build();
    }
}
