package com.directors.domain.room;

import com.directors.infrastructure.exception.room.RoomNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
@Builder
public class Room {
    private Long id;
    private Long questionId;
    private String directorId;
    private String questionerId;
    private LocalDateTime createTime;

    public void setId(Long id) {
        this.id = id;
    }

    public static Room of(Long questionId, String directorId, String questionerId) {
        return Room.builder()
                .questionId(questionId)
                .directorId(directorId)
                .questionerId(questionerId)
                .createTime(LocalDateTime.now())
                .build();
    }

    public void validateRoomUser(String sendUserId) {
        if (!(sendUserId.equals(directorId) || sendUserId.equals(questionerId))) {
            throw new RoomNotFoundException(this.id, sendUserId);
        }
    }
}
