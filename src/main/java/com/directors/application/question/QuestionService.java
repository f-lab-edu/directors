package com.directors.application.question;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;
import com.directors.domain.question.QuestionStatus;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.domain.user.exception.NoSuchUserException;
import com.directors.infrastructure.exception.ExceptionCode;
import com.directors.infrastructure.exception.question.QuestionDuplicateException;
import com.directors.infrastructure.exception.question.QuestionNotFoundException;
import com.directors.infrastructure.exception.schedule.InvalidMeetingRequest;
import com.directors.presentation.question.request.CreateQuestionRequest;
import com.directors.presentation.question.request.EditQuestionRequest;
import com.directors.presentation.question.response.ReceivedQuestionResponse;
import com.directors.presentation.question.response.SentQuestionResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final QuestionRepository questionRepository;
	private final ScheduleRepository scheduleRepository;
	private final UserRepository userRepository;

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
		boolean isExists = questionRepository.existsByQuestionerIdAndDirectorId(questionerId,
				request.getDirectorId());
		// 동일한 디렉터에게 질문 불가능.
		if (isExists) {
			throw new QuestionDuplicateException(ExceptionCode.QuestionDuplicated, questionerId);
		}

		User questioner = getUserById(questionerId);
		User director = getUserById(request.getDirectorId());

		Question question = Question.builder()
				.title(request.getTitle())
				.content(request.getContent())
				.status(QuestionStatus.WAITING)
				.questionCheck(false)
				.directorCheck(false)
				.questioner(questioner)
				.director(director)
				.category(request.getCategory())
				.schedule(schedule)
				.build();

		questionRepository.save(question);
	}

	@Transactional
	public void edit(Long questionId, EditQuestionRequest editQuestionRequest) {
		Question question = questionRepository.findById(questionId)
				.orElseThrow(() -> new QuestionNotFoundException(ExceptionCode.QuestionNotFound, questionId));

		question.checkUneditableStatus();

		// 예약시간이 변경되었을 경우에 처리.
		boolean isChangedTime = question.isChangedTime(editQuestionRequest.getStartTime());

		if (isChangedTime) {
			// 변경하는 시간대 validation
			Schedule schedule = validateTime(editQuestionRequest.getStartTime(), editQuestionRequest.getDirectorId());
			question.changeSchedule(schedule);
		}

		question.editQuestion(editQuestionRequest.getTitle(), editQuestionRequest.getContent());
		questionRepository.save(question);
	}

	private Schedule validateTime(LocalDateTime startTime, String userId) {
		Schedule schedule = scheduleRepository.findByStartTimeAndUserId(startTime, userId)
				.orElseThrow(() -> new InvalidMeetingRequest(ExceptionCode.InvalidMeetingTime, startTime, userId));

		schedule.checkChangeableScheduleTime();
		return schedule;
	}

	private User getUserById(String questionerId) {
		return userRepository.findByIdAndUserStatus(questionerId, UserStatus.JOINED)
				.orElseThrow(() -> new NoSuchUserException(questionerId));
	}
}