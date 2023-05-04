package com.directors.infrastructure.jpa.schedule;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.schedule.ScheduleStatus;

public class InmemoryScheduleRepository implements ScheduleRepository {
	private final Map<Long, Schedule> map = new HashMap<>();

	private Long id = 1234L;

	@Override
	public Optional<Schedule> findByStartTimeAndUserId(LocalDateTime startTime, String userId) {
		Optional<Schedule> optionalSchedule = map.values()
			.stream()
			.filter(
				schedule -> (schedule.getUser().getId().equals(userId) && schedule.getStartTime().isEqual(startTime)))
			.findFirst();
		return optionalSchedule;
	}

	@Override
	public List<Schedule> findByUserIdAndStatus(String userId, ScheduleStatus status) {
		return map.values()
			.stream()
			.filter(
				schedule -> schedule.getUser().getId().equals(userId) && schedule.getStatus().equals(status))
			.collect(Collectors.toList());
	}

	@Override
	public boolean existsByUserIdAndStatus(String userId, ScheduleStatus scheduleStatus) {
		return map.values()
			.stream()
			.anyMatch(sc -> sc.getStatus().equals(scheduleStatus) && sc.getUser().getId().equals(userId));
	}

	@Override
	public void save(Schedule schedule) {
		map.put(schedule.getId(), schedule);
	}

	@Override
	public void delete(Schedule schedule) {
		map.remove(schedule.getId());
	}
}
