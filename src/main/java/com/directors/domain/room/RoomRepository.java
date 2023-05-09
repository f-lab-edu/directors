package com.directors.domain.room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    Optional<Room> findById(Long roomId);

    List<Room> findByDirectorId(String directorId);

    List<Room> findByQuestionerId(String questionerId);

    Room save(Room room);
}
