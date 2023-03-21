package com.directors.application.question;

import java.util.List;

import org.springframework.stereotype.Service;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;
import com.directors.domain.question.QuestionStatus;
import com.directors.domain.user.UserRepository;
import com.directors.presentation.qeustion.response.ReceivedQuestionResponse;
import com.directors.presentation.qeustion.response.SentQuestionResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final QuestionRepository questionRepository;
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
}
