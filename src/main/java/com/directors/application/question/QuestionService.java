package com.directors.application.question;

import java.util.List;

import org.springframework.stereotype.Service;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;
import com.directors.presentation.qeustion.response.QuestionResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final QuestionRepository questionRepository;

	public List<QuestionResponseDto> getSendList(String questionerID) {
		List<Question> sentQuestions = questionRepository.findByQuestionerId(questionerID);
		return getQuestionResponseDtos(sentQuestions);
	}

	public List<QuestionResponseDto> getReceiveList(String directorId) {
		List<Question> receivedQuestions = questionRepository.findByDirectorId(directorId);
		return getQuestionResponseDtos(receivedQuestions);
	}

	private List<QuestionResponseDto> getQuestionResponseDtos(List<Question> sentQuestions) {
		return sentQuestions.stream()
			.map(QuestionResponseDto::from)
			.toList();
	}
}
