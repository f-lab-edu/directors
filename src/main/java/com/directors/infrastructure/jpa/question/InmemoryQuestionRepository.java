package com.directors.infrastructure.jpa.question;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionStatus;

public class InmemoryQuestionRepository {
	private final Map<Long, Question> questionMap = new HashMap<>();

	private static Long questionId = 3000L;

	public List<Question> findByDirectorId(String directorId) {
		return questionMap.values()
			.stream()
			.filter(question -> question.getDirector().getId().equals(directorId))
			.collect(Collectors.toList());
	}

	public List<Question> findByQuestionerId(String questionerID) {
		return questionMap.values()
			.stream()
			.filter(question -> question.getQuestioner().getId().equals(questionerID))
			.collect(Collectors.toList());
	}

	public Question save(Question question) {
		boolean isNewQuestion = question.isNewQuestion();
		if (isNewQuestion) {
			question.setId(++questionId);
		}

		questionMap.put(question.getId(), question);

		return question;
	}

	public Optional<Question> findById(Long questionId) {
		return Optional.ofNullable(questionMap.get(questionId));
	}

	public boolean existsByQuestionerIdAndDirectorId(String questionerId, String directorId) {
		return questionMap.values()
			.stream()
			.filter(question -> question.getQuestioner().getId().equals(questionerId) && question.getDirector().getId()
				.equals(directorId))
			.findAny()
			.isPresent();
	}

	public boolean existsByDirectorIdAndStartTimeAndStatus(String directorId, LocalDateTime startTime,
		QuestionStatus status) {
		return false;
	}

}
