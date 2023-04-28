package com.directors.domain.feedback;

import java.util.Optional;

public interface FeedbackRepository {
    Optional<Feedback> findById(Long id);

    Feedback save(Feedback feedback);
}
