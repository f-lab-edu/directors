package com.directors.infrastructure.jpa.schedule;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.schedule.ScheduleStatus;

@Repository
public class InmemoryScheduleRepository implements ScheduleRepository {
	private Long id = 1234L;
	private final Map<Long, Schedule> map = new HashMap<>() {
		{
			put(1230L, new Schedule(1230L, LocalDateTime.of(2023, 4, 13, 14, 0), ScheduleStatus.OPENED, "dohyun123"));
			put(1231L, new Schedule(1231L, LocalDateTime.of(2023, 4, 11, 20, 0), ScheduleStatus.CLOSED, "dohyun123"));
		}
	};

	@Override
	public Optional<Schedule> findByStartTimeAndUserId(LocalDateTime startTime, String userId) {
		Optional<Schedule> optionalSchedule = map.values()
			.stream()
			.filter(schedule -> (schedule.getUserId().equals(userId) && schedule.getStartTime().isEqual(startTime)))
			.findFirst();
		return optionalSchedule;
	}

	@Override
	public void save(Schedule schedule) {
		map.put(schedule.getScheduleId(), schedule);
	}
}
