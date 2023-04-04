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
import com.directors.infrastructure.exception.question.InvalidQuestionStatusException;
import com.directors.infrastructure.exception.question.QuestionNotFoundException;
import com.directors.infrastructure.exception.schedule.InvalidMeetingRequest;
import com.directors.presentation.question.request.CreateQuestionRequest;
import com.directors.presentation.question.request.EditQuestionRequest;
import com.directors.presentation.question.response.ReceivedQuestionResponse;
import com.directors.presentation.question.response.SentQuestionResponse;

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
		Schedule schedule = validateTime(request.getStartTime(), request.getDirectorId());
		schedule.changeStatus(ScheduleStatus.CLOSED);
		scheduleRepository.save(schedule);

		//적절한 question class 만들어서 생성
		questionRepository.save(Question.of(request.getTitle(), request.getContent(), request.getDirectorId(),
			request.getCategory(), request.getStartTime(), questionerId, schedule.getScheduleId()));
	}

	@Transactional
	public void edit(Long questionId, EditQuestionRequest editQuestionRequest) {
		Question question = questionRepository.findByQuestionId(questionId)
			.orElseThrow(() -> {
				throw new QuestionNotFoundException(ExceptionCode.QuestionNotFound, questionId);
			});

		if (question.getStatus() != QuestionStatus.WAITING) {
			throw new InvalidQuestionStatusException(ExceptionCode.InvalidQuestionStatus, question.getId(),
				question.getStatus());
		}

		// 예약시간이 변경되었을 경우에 처리.
		if (!question.getStartTime().isEqual(editQuestionRequest.getStartTime())) {
			// 변경하는 시간대 validation 후 closed 처리.
			Schedule newSchedule = validateTime(editQuestionRequest.getStartTime(),
				editQuestionRequest.getDirectorId());
			newSchedule.changeStatus(ScheduleStatus.CLOSED);
			scheduleRepository.save(newSchedule);

			//원래 시간대 open 처리
			Schedule oldSchedule = scheduleRepository.findByStartTimeAndUserId(question.getStartTime(),
				question.getDirectorId()).get();
			oldSchedule.changeStatus(ScheduleStatus.OPENED);
			scheduleRepository.save(oldSchedule);

			question.changeStartTime(newSchedule.getStartTime());
		}

		question.changeTitleAndContent(editQuestionRequest.getTitle(), editQuestionRequest.getContent());
		questionRepository.save(question);
	}

	private Schedule validateTime(LocalDateTime startTime, String userId) {
		Schedule schedule = scheduleRepository.findByStartTimeAndUserId(startTime, userId)
			.orElseThrow(() -> {
				throw new InvalidMeetingRequest(ExceptionCode.InvalidMeetingTime, startTime, userId);
			});

		if (schedule.getStatus() != ScheduleStatus.OPENED) {
			throw new InvalidMeetingRequest(ExceptionCode.ClosedSchedule, startTime, userId);
		}
		return schedule;
	}
}