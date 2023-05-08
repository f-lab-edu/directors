package com.directors.infrastructure.jpa.question;

import static com.directors.domain.question.QQuestion.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;
import com.directors.domain.question.QuestionStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryAdapter implements QuestionRepository {
	private final JpaQuestionRepository jpaQuestionRepository;
	private final JPAQueryFactory queryFactory;

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

	@Override
	public boolean existsByDirectorIdAndStartTimeAndStatus(String directorId, LocalDateTime startTime,
		QuestionStatus status) {
		Integer fetchOne = queryFactory
			.selectOne()
			.from(question)
			.where(question.director.id.eq(directorId), question.schedule.startTime.eq(startTime),
				question.status.eq(status))
			.fetchFirst();

		return fetchOne != null;
	}
}
