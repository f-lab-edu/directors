package com.directors.infrastructure.jpa.room;

import com.directors.domain.room.Room;
import com.directors.domain.room.RoomRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InmemeoryRoomRepository implements RoomRepository {
    Map<Long, Room> roomMap = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public Optional<Room> findById(Long roomId) {
        return Optional.ofNullable(roomMap.get(roomId));
    }

    @Override
    public List<Room> findByDirectorId(String directorId) {
        return roomMap.values().stream()
                .filter(room -> directorId.equals(room.getDirector().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findByQuestionerId(String questionerId) {
        return roomMap.values().stream()
                .filter(room -> questionerId.equals(room.getQuestioner().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Room save(Room room) {
        if (room.getId() == null) {
            room.setId(nextId++);
        }
        roomMap.put(room.getId(), room);

        return room;
    }
}
