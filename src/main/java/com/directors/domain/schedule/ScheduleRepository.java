package com.directors.domain.schedule;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleRepository {
	Optional<Schedule> findByStartTimeAndUserId(LocalDateTime startTime, String userId);

	void save(Schedule schedule);
}
