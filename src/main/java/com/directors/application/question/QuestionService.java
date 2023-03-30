package com.directors.application.question;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;
import com.directors.domain.question.QuestionStatus;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.schedule.ScheduleStatus;
import com.directors.domain.user.UserRepository;
import com.directors.infrastructure.exception.ExceptionCode;
import com.directors.infrastructure.exception.schedule.InvalidMeetingRequest;
import com.directors.presentation.qeustion.request.CreateQuestionRequest;
import com.directors.presentation.qeustion.response.ReceivedQuestionResponse;
import com.directors.presentation.qeustion.response.SentQuestionResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final QuestionRepository questionRepository;
	private final UserRepository userRepository;
	private final ScheduleRepository scheduleRepository;

	public List<SentQuestionResponse> getSendList(String questionerID) {
		List<Question> sentQuestions = questionRepository.findByQuestionerId(questionerID);
		return sentQuestions.stream()
			.map(question -> SentQuestionResponse.from(question))
			.toList();
	}

	public List<ReceivedQuestionResponse> getReceiveList(String directorId) {
		List<Question> receivedQuestions = questionRepository.findByDirectorId(directorId);
		return receivedQuestions.stream()
			.filter(question -> question.getStatus() != QuestionStatus.COMPLETE)
			.map(question -> ReceivedQuestionResponse.from(question))
			.toList();
	}

	@Transactional
	public void create(CreateQuestionRequest request, String questionerId) {
		//시간이 올바른지 확인, userId로부터 schedule 가져오기.
		validateTime(request.getStartTime(), request.getDirectorId());

		//schedule 추가
		Long scheduleId = 1234L;
		scheduleRepository.save(
			Schedule.of(scheduleId, request.getStartTime(), ScheduleStatus.CLOSED, request.getDirectorId()));

		//적절한 question class 만들어서 생성
		questionRepository.save(Question.of(request.getTitle(), request.getContent(), request.getDirectorId(),
			request.getCategory(), request.getStartTime(), questionerId, scheduleId));
	}

	private void validateTime(LocalDateTime startTime, String userId) {
		Schedule schedule = scheduleRepository.findByStartTimeAndUserId(startTime, userId)
			.orElseThrow(() -> {
				throw new InvalidMeetingRequest(ExceptionCode.InvalidMeetingTime, startTime, userId);
			});

		if (schedule.getStatus() != ScheduleStatus.OPENED) {
			throw new InvalidMeetingRequest(ExceptionCode.ClosedSchedule, startTime, userId);
		}

	}
}