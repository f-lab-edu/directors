package com.directors.application.question;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;
import com.directors.domain.question.QuestionStatus;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.specialty.SpecialtyProperty;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.domain.user.exception.NoSuchUserException;
import com.directors.infrastructure.exception.ExceptionCode;
import com.directors.infrastructure.exception.question.QuestionDuplicateException;
import com.directors.infrastructure.exception.question.QuestionNotFoundException;
import com.directors.infrastructure.exception.schedule.InvalidMeetingRequest;
import com.directors.presentation.question.request.CreateQuestionRequest;
import com.directors.presentation.question.request.DeclineQuestionRequest;
import com.directors.presentation.question.request.EditQuestionRequest;
import com.directors.presentation.question.response.DetailQuestionResponse;
import com.directors.presentation.question.response.ReceivedQuestionResponse;
import com.directors.presentation.question.response.SentQuestionResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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
			.category(SpecialtyProperty.fromValue(request.getCategory()))
			.schedule(schedule)
			.build();

		questionRepository.save(question);
	}

	@Transactional
	public void edit(Long questionId, EditQuestionRequest editQuestionRequest) {
		Question question = getQuestionById(questionId);

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

	public DetailQuestionResponse getQuestionDetail(Long questionId) {
		Question question = getQuestionById(questionId);

		return DetailQuestionResponse.from(question);
	}

	@Transactional
	public void decline(Long questionId, String userId, DeclineQuestionRequest declineQuestionRequest) {
		Question question = getQuestionById(questionId);

		//Waitting 상태의 질문만 거절 가능
		question.checkUneditableStatus();

		question.decline(userId, declineQuestionRequest.getComment());

		//질문자 리워드 증가.
		User questioner = question.getQuestioner();
		questioner.addReword();

	}

	@Transactional
	public void accept(Long questionId, String userId) {
		Question question = getQuestionById(questionId);
		question.checkUneditableStatus();

		question.accept(userId);

		//schedule close 처리
		Schedule schedule = question.getSchedule();
		schedule.closeSchedule();
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

	private Question getQuestionById(Long questionId) {
		return questionRepository.findById(questionId)
			.orElseThrow(() -> new QuestionNotFoundException(ExceptionCode.QuestionNotFound, questionId));
	}
}