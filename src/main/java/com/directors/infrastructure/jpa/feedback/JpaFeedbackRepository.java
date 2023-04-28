package com.directors.infrastructure.jpa.feedback;

import com.directors.domain.feedback.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFeedbackRepository extends JpaRepository<Feedback, Long> {
}
