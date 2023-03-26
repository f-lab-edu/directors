package com.directors.infrastructure.jpa.schedule;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;

public class InmemoryScheduleRepository implements ScheduleRepository {
	private final Map<Long, Schedule> map = new HashMap<>();

	@Override
	public Optional<Schedule> findByStartTimeAndUserId(LocalDateTime startTime, String userId) {

		return Optional.empty();
	}
}
