package com.directors.domain.schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
	Optional<Schedule> findByStartTimeAndUserId(LocalDateTime startTime, String userId);

	List<Schedule> findByUserIdAndStatus(String userId, ScheduleStatus status);

	boolean existsByUserIdAndStatus(String userId, ScheduleStatus Status);

	Schedule save(Schedule schedule);

	List<Schedule> saveAll(List<Schedule> scheduleList);

	void delete(Schedule schedule);
}
