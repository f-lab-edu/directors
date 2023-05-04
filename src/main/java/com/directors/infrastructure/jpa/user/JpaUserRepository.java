package com.directors.infrastructure.jpa.user;

import com.directors.domain.user.User;
import com.directors.domain.user.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, String> {
    Optional<User> findByIdAndUserStatus(String id, UserStatus userStatus);

    @Query(
            value = "SELECT * FROM user u " +
                    "INNER JOIN user_region ur ON u.id = ur.user_id " +
                    "LEFT JOIN specialty s ON u.id = s.user_id " +
                    "LEFT JOIN schedule sc ON u.id = sc.user_id " +
                    "WHERE ur.user_id IN (:regionIds) " +
                    "AND u.user_status = 'JOINED' " +
                    "AND (u.name LIKE CONCAT('%', :searchText, '%') OR u.nickname LIKE CONCAT('%', :searchText, '%')) " +
                    "AND s.description LIKE CONCAT('%', :property, '%') " +
                    "AND (:hasSchedule = false OR (sc.startTime > :currentTime AND sc.status = 'OPENED')) " +
                    "LIMIT :limit OFFSET :offset",
            nativeQuery = true
    )
    List<User> findWithSearchConditions(
            @Param("regionIds") List<Long> regionIds,
            @Param("hasSchedule") boolean hasSchedule,
            @Param("searchText") String searchText,
            @Param("property") String property,
            @Param("offset") int offset,
            @Param("limit") int limit
    );
}
