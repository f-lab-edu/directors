package com.directors.infrastructure.jpa.question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;

@Repository
public class InmemoryQuestionRepository implements QuestionRepository {
	private final Map<Long, Question> questionMap = new HashMap<>();

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
}
