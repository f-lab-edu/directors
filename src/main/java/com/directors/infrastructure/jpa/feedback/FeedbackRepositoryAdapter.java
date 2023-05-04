package com.directors.infrastructure.jpa.feedback;

import com.directors.domain.feedback.Feedback;
import com.directors.domain.feedback.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FeedbackRepositoryAdapter implements FeedbackRepository {
    private final JpaFeedbackRepository jpaFeedbackRepository;

    @Override
    public Optional<Feedback> findById(Long id) {
        return jpaFeedbackRepository.findById(id);
    }

    @Override
    public Feedback save(Feedback feedback) {
        return jpaFeedbackRepository.save(feedback);
    }
}
