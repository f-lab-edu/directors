package com.directors.infrastructure.jpa.question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;

public class InmemoryQuestionRepository implements QuestionRepository {
	private final Map<Long, Question> questionMap = new HashMap<>();

	private static Long questionId = 3000L;

	@Override
	public List<Question> findByDirectorId(String directorId) {
		return questionMap.values()
			.stream()
			.filter(question -> question.getDirectorId().equals(directorId))
			.collect(Collectors.toList());
	}

	@Override
	public List<Question> findByQuestionerId(String questionerID) {
		return questionMap.values()
			.stream()
			.filter(question -> question.getQuestionerId().equals(questionerID))
			.collect(Collectors.toList());
	}

	@Override
	public Question save(Question question) {
		boolean isNewQuestion = question.isNewQuestion();
		if (isNewQuestion) {
			question.setId(++questionId);
		}

		questionMap.put(question.getId(), question);

		return question;
	}

	@Override
	public Optional<Question> findById(Long questionId) {
		return Optional.ofNullable(questionMap.get(questionId));
	}

	@Override
	public boolean existsByQuestionerIdAndDirectorId(String questionerId, String directorId) {
		return questionMap.values()
			.stream()
			.filter(question -> question.getQuestionerId().equals(questionerId) && question.getDirectorId()
				.equals(directorId))
			.findAny()
			.isPresent();
	}
}
