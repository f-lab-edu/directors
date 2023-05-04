package com.directors.application.schedule;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.directors.domain.question.QuestionRepository;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.schedule.ScheduleStatus;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.exception.NoSuchUserException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private final ScheduleRepository scheduleRepository;
	private final QuestionRepository questionRepository;
	private final UserRepository userRepository;

	@Transactional
	public void open(String userId, List<LocalDateTime> startTimeList) {
		User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchUserException(userId));
		for (LocalDateTime startTime : startTimeList) {
			Schedule schedule = scheduleRepository.findByStartTimeAndUserId(startTime, userId)
				.orElse(Schedule.of(startTime, ScheduleStatus.OPENED, user));

			//해당시간에 예약중인 schedule이 존재하는 경우 예외 발생시켜야함.
			//구현예정

			schedule.openSchedule();
			scheduleRepository.save(schedule);
		}
	}
}
