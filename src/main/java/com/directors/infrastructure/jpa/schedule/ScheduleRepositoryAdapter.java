package com.directors.infrastructure.jpa.schedule;

import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.schedule.ScheduleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryAdapter implements ScheduleRepository {
    private final JpaScheduleRepository jpaScheduleRepository;

    @Override
    public Optional<Schedule> findByStartTimeAndUserId(LocalDateTime startTime, String userId) {
        return jpaScheduleRepository.findByStartTimeAndUserId(startTime, userId);
    }

    @Override
    public List<Schedule> findByUserIdAndStatus(String userId, ScheduleStatus status) {
        return jpaScheduleRepository.findByUserIdAndStatus(userId, status);
    }

    @Override
    public boolean existsByUserIdAndStatus(String userId, ScheduleStatus scheduleStatus) {
        return jpaScheduleRepository.existsByUserIdAndStatus(userId, scheduleStatus);
    }

    @Override
    public Schedule save(Schedule schedule) {
        return jpaScheduleRepository.save(schedule);
    }

    @Override
    public void delete(Schedule schedule) {
        jpaScheduleRepository.delete(schedule);
    }
}
