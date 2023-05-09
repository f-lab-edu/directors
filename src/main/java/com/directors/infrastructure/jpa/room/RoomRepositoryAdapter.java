package com.directors.infrastructure.jpa.room;

import com.directors.domain.room.Room;
import com.directors.domain.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryAdapter implements RoomRepository {
    private final JpaRoomRepository jpaRoomRepository;

    @Override
    public Optional<Room> findById(Long roomId) {
        return jpaRoomRepository.findById(roomId);
    }

    @Override
    public List<Room> findByDirectorId(String directorId) {
        return jpaRoomRepository.findByDirectorId(directorId);
    }

    @Override
    public List<Room> findByQuestionerId(String questionerId) {
        return jpaRoomRepository.findByQuestionerId(questionerId);
    }

    @Override
    public Room save(Room room) {
        return jpaRoomRepository.save(room);
    }
}
