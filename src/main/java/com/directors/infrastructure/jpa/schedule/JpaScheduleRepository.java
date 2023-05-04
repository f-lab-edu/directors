package com.directors.infrastructure.jpa.schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleStatus;

public interface JpaScheduleRepository extends JpaRepository<Schedule, Long> {
	Optional<Schedule> findByStartTimeAndUserId(LocalDateTime startTime, String userId);

	List<Schedule> findByUserIdAndStatus(String userId, ScheduleStatus status);

	boolean existsByUserIdAndStatus(String userId, ScheduleStatus status);
}
