package com.directors.infrastructure.jpa.question;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryAdapter implements QuestionRepository {
	private final JpaQuestionRepository jpaQuestionRepository;

	@Override
	public List<Question> findByDirectorId(String directorId) {
		return jpaQuestionRepository.findByDirectorId(directorId);
	}

	@Override
	public List<Question> findByQuestionerId(String questionerId) {
		return jpaQuestionRepository.findByQuestionerId(questionerId);
	}

	@Override
	public Question save(Question question) {
		return jpaQuestionRepository.save(question);
	}

	@Override
	public Optional<Question> findById(Long id) {
		return jpaQuestionRepository.findById(id);
	}

	@Override
	public boolean existsByQuestionerIdAndDirectorId(String questionerId, String directorId) {
		return jpaQuestionRepository.existsByQuestionerIdAndDirectorId(questionerId, directorId);
	}
}
