package com.directors.domain.room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Optional<Room> findByQuestionId(Long questionId);

    List<Room> findByDirectorId(String directorId);

    List<Room> findByQuestionerId(String questionerId);

    void save(Room room);
}
