package com.directors.domain.question;

import java.util.List;

public interface QuestionRepository {

	List<Question> findByDirectorId(String directorId);

	List<Question> findByQuestionerId(String questionerId);

	Question save(Question question);
}
