package com.directors.domain.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

	List<Question> findByDirectorId(String directorId);

	List<Question> findByQuestionerId(String questionerId);

	Question save(Question question);

	Optional<Question> findById(Long id);

	boolean existsByQuestionerIdAndDirectorId(String questionerId, String directorId);

	boolean existsByDirectorIdAndStartTimeAndStatus(String directorId, LocalDateTime startTime,
		QuestionStatus status);
}
