package com.directors.domain.room;

import com.directors.domain.room.exception.RoomNotFoundException;
import com.directors.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
    @DisplayName("채팅방의 사용자인지 확인한다.")
    @Test
    void validateRoomUser() {
        // given
        String givenUserId = "directorId";
        User director = User.builder().id("directorId").build();
        User questioner = User.builder().id("questionerId").build();

        Room room = Room.builder()
                .id(1L)
                .director(director)
                .questioner(questioner)
                .build();

        // when
        room.validateRoomUser(givenUserId);

        // then
        assertFalse(Thread.currentThread().isInterrupted());
    }

    @DisplayName("채팅방의 사용자가 아니면 예외가 발생한다.")
    @Test
    void validateRoomUserWithWrongUserId() {
        // given
        String givenWrongUserId = "wrongUserId";
        User director = User.builder().id("directorId").build();
        User questioner = User.builder().id("questionerId").build();

        Room room = Room.builder()
                .id(1L)
                .director(director)
                .questioner(questioner)
                .build();

        // when then
        assertThatThrownBy(() -> room.validateRoomUser(givenWrongUserId))
                .isInstanceOf(RoomNotFoundException.class)
                .hasMessage("존재하지 않는 채팅방입니다.");
    }
}