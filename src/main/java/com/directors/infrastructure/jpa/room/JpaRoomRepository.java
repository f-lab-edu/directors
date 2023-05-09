package com.directors.infrastructure.jpa.room;

import com.directors.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaRoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByDirectorId(String directorId);

    List<Room> findByQuestionerId(String questionerId);
}
