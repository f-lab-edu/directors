package com.directors.domain.room;

import com.directors.domain.schedule.ScheduleStatus;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    Optional<Room> findById(Long roomId);

    List<Room> findByDirectorId(String directorId);

    List<Room> findByQuestionerId(String questionerId);

    boolean existsByQuestionId(Long questionId);

    Room save(Room room);
}
