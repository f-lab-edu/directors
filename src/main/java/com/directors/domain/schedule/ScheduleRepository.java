package com.directors.domain.schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    Optional<Schedule> findByStartTimeAndUserId(LocalDateTime startTime, String userId);

    List<Schedule> findByUserIdAndScheduleStatus(String userId, ScheduleStatus scheduleStatus);

    boolean existsByUserIdAndScheduleStatus(String userId, ScheduleStatus scheduleStatus);

    void save(Schedule schedule);
}
