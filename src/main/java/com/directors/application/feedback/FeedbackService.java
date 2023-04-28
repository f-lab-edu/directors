package com.directors.application.feedback;

import com.directors.domain.feedback.Feedback;
import com.directors.domain.feedback.FeedbackRating;
import com.directors.domain.feedback.FeedbackRepository;
import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.domain.user.exception.NoSuchUserException;
import com.directors.infrastructure.exception.question.QuestionNotFoundException;
import com.directors.presentation.feedback.request.CreateFeedbackRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;

    public Object create(CreateFeedbackRequest request, String questionerId) {
        Question question = getQuestionById(request.questionId());

        question.canCreateFeedback(questionerId);

        // TODO: 04.28 Question JPA 적용 시 Fetch Join 통해 변경
        User questioner = getUserByUserId(questionerId);
        User director = getUserByUserId(question.getDirectorId());

        Feedback feedback = Feedback.builder()
                .feedbackRating(FeedbackRating.fromValue(request.rating()))
                .description(request.description())
                .questioner(questioner)
                .director(director)
                .feedbackCheckList(request.toFeedbackCheckList())
                .build();

        return feedbackRepository.save(feedback);
    }

    private User getUserByUserId(String userId) {
        return userRepository
                .findByIdAndUserStatus(userId, UserStatus.JOINED)
                .orElseThrow(() -> new NoSuchUserException(userId));
    }

    private Question getQuestionById(Long questionId) {
        return questionRepository
                .findByQuestionId(questionId)
                .orElseThrow(QuestionNotFoundException::new);
    }
}