package com.directors.infrastructure.jpa.chat;

import com.directors.domain.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaChatRepository extends JpaRepository<Chat, Long> {
    @Query(
            value = "SELECT c.content, c.createdTime FROM chat c " +
                    "LEFT JOIN room r ON c.room_id = r.id " +
                    "WHERE c.room_id = :roomId " +
                    "ORDER BY c.createdTime DESC " +
                    "LIMIT :limit OFFSET :offset",
            nativeQuery = true
    )
    List<Chat> findListByRoomId(
            @Param("roomId") Long roomId,
            @Param("offset") int offset,
            @Param("limit") int limit
    );
}
