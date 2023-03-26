package com.directors.application.question;

import java.util.List;

import org.springframework.stereotype.Service;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;
import com.directors.domain.question.QuestionStatus;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.schedule.ScheduleStatus;
import com.directors.domain.user.UserRepository;
import com.directors.infrastructure.exception.question.ClosedScheduleException;
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
		//시간이 올바른지 확인, 못하면 Exception 던지기, userId로부터 schedule 가져오기.
		Schedule schedule = scheduleRepository.findByStartTimeAndUserId(request.getStartTime(), questionerId);
		if (schedule.getStatus() != ScheduleStatus.OPENED) {
			throw new ClosedScheduleException("이미 예약된 시간입니다.");
		}
		Long scheduleId = 1234L;
		//적절한 question class 만들어서 생성
		questionRepository.save(QuestionConverter.of(request, questionerId, scheduleId));
	}
}
