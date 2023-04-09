package com.directors.infrastructure.jpa.question;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;
import com.directors.domain.question.QuestionStatus;

@Repository
public class InmemoryQuestionRepository implements QuestionRepository {
	private final Map<Long, Question> questionMap = new HashMap<>() {
		{
			put(1234L, new Question(1234L, LocalDateTime.now(), "hello", "f-lab, mentoring",
				QuestionStatus.WAITING, false, false, "dummyUser",
				"song0209", "reading", 1123L,
				LocalDateTime.of(2023, Month.APRIL, 12, 9, 0)));
			put(3562L, new Question(3541L, LocalDateTime.now(), "nice", "contents",
				QuestionStatus.CHATTING, false, false, "song0209",
				"dummyUser", "fishing", 4431L,
				LocalDateTime.of(2023, Month.APRIL, 3, 14, 0)));
			put(6543L, new Question(6543L, LocalDateTime.now(), "haha", "contents12",
				QuestionStatus.COMPLETE, false, false, "dummyUser",
				"dohyun12", "cook", 4431L,
				LocalDateTime.of(2023, Month.APRIL, 3, 20, 0)));
			put(2324L, new Question(2324L, LocalDateTime.now(), "good", "contents44",
				QuestionStatus.COMPLETE, false, false, "dohyun12",
				"dummyUser", "drawing", 4431L,
				LocalDateTime.of(2023, Month.APRIL, 2, 20, 0)));
		}
	};

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
	public Optional<Question> findByQuestionId(Long questionId) {
		return Optional.ofNullable(questionMap.get(questionId));
	}

	@Override
	public Optional<Question> findByQuestionIdAndDirectorId(String questionerId, String directorId) {
		return questionMap.values()
			.stream()
			.filter(question -> question.getQuestionerId().equals(questionerId) && question.getDirectorId()
				.equals(directorId))
			.findFirst();
	}
}
