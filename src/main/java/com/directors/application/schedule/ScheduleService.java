package com.directors.application.schedule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.directors.domain.question.QuestionRepository;
import com.directors.domain.question.QuestionStatus;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.schedule.ScheduleStatus;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.exception.NoSuchUserException;
import com.directors.infrastructure.exception.schedule.InvalidChangeScheduleException;

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

		List<Schedule> list = new ArrayList<>();
		for (LocalDateTime startTime : startTimeList) {
			Schedule schedule = scheduleRepository.findByStartTimeAndUserId(startTime, userId)
				.orElse(Schedule.of(startTime, ScheduleStatus.OPENED, user));

			boolean isNewSchedule = schedule.isNewSchedule();

			if (isNewSchedule) {
				list.add(schedule);
				continue;
			}
			//해당시간에 예약중인 약속이 존재하는 경우 예외 발생시켜야함.
			//받은 질문중 해당 시간에 question 상태가 chatting인게 있는지 확인
			checkQuestionForChangeSchedule(userId, startTime, QuestionStatus.CHATTING);

			schedule.openSchedule();
		}

		scheduleRepository.saveAll(list);
	}

	@Transactional
	public void close(String userId, List<LocalDateTime> startTimeList) {
		User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchUserException(userId));
		List<Schedule> list = new ArrayList<>();
		for (LocalDateTime startTime : startTimeList) {
			Schedule schedule = scheduleRepository.findByStartTimeAndUserId(startTime, userId)
				.orElse(Schedule.of(startTime, ScheduleStatus.CLOSED, user));

			boolean isNewSchedule = schedule.isNewSchedule();
			if (isNewSchedule) {
				list.add(schedule);
				continue;
			}

			checkQuestionForChangeSchedule(userId, startTime, QuestionStatus.WAITING);

			schedule.closeSchedule();
			//scheduleRepository.save(schedule);
		}

		scheduleRepository.saveAll(list);
	}

	private void checkQuestionForChangeSchedule(String userId, LocalDateTime startTime, QuestionStatus status) {
		boolean existsChattingQuestion = questionRepository.existsByDirectorIdAndStartTimeAndStatus(userId,
			startTime,
			status);

		if (existsChattingQuestion) {
			throw new InvalidChangeScheduleException(startTime, status);
		}
	}
}
