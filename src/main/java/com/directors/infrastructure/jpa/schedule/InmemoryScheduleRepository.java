package com.directors.infrastructure.jpa.schedule;

import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.schedule.ScheduleStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InmemoryScheduleRepository implements ScheduleRepository {
    private final Map<Long, Schedule> map = new HashMap<>() {
      {
        put(1230L, new Schedule(1230L, LocalDateTime.of(2023, 4, 13, 14, 0), ScheduleStatus.OPENED, "dohyun123"));
        put(1231L, new Schedule(1231L, LocalDateTime.of(2023, 4, 11, 20, 0), ScheduleStatus.CLOSED, "dohyun123"));
        put(1232L, new Schedule(1232L, LocalDateTime.of(2023, 4, 12, 14, 0), ScheduleStatus.OPENED, "dohyun123"));
      }
    };

    private Long id = 1234L;

    @Override
    public Optional<Schedule> findByStartTimeAndUserId(LocalDateTime startTime, String userId) {
        Optional<Schedule> optionalSchedule = map.values()
                .stream()
                .filter(schedule -> (schedule.getUserId().equals(userId) && schedule.getStartTime().isEqual(startTime)))
                .findFirst();
        return optionalSchedule;
    }

    @Override
    public List<Schedule> findByUserIdAndScheduleStatus(String userId, ScheduleStatus scheduleStatus) {
        return map.values()
                .stream()
                .filter(schedule -> schedule.getUserId().equals(userId) && schedule.getStatus().equals(scheduleStatus))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsByUserIdAndScheduleStatus(String userId, ScheduleStatus scheduleStatus) {
        return map.values()
                .stream()
                .anyMatch(sc -> sc.getStatus().equals(scheduleStatus) && sc.getUserId().equals(userId));
    }

    @Override
    public void save(Schedule schedule) {
        map.put(schedule.getScheduleId(), schedule);
    }
}
