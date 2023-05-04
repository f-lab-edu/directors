package com.directors.application.feedback;

import com.directors.domain.feedback.Feedback;
import com.directors.domain.feedback.FeedbackRating;
import com.directors.domain.feedback.FeedbackRepository;
import com.directors.domain.feedback.exception.FeedbackNotFoundException;
import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;
import com.directors.infrastructure.exception.question.QuestionNotFoundException;
import com.directors.presentation.feedback.request.CreateFeedbackRequest;
import com.directors.presentation.feedback.request.UpdateFeedbackRequest;
import com.directors.presentation.feedback.response.CreateFeedbackResponse;
import com.directors.presentation.feedback.response.GetByFeedbackIdResponse;
import com.directors.presentation.feedback.response.UpdateFeedbackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final QuestionRepository questionRepository;
    private final FeedbackRepository feedbackRepository;

    @Transactional
    public CreateFeedbackResponse create(CreateFeedbackRequest request, String questionerId) {
        var question = getQuestionById(request.questionId());

        question.canCreateFeedback(questionerId);

        var feedback = Feedback.builder()
                .feedbackRating(FeedbackRating.fromValue(request.rating()))
                .description(request.description())
                .questioner(question.getQuestioner())
                .director(question.getDirector())
                .question(question)
                .feedbackCheckList(request.toFeedbackCheckList())
                .build();

        return CreateFeedbackResponse.from(feedbackRepository.save(feedback));
    }

    @Transactional
    public UpdateFeedbackResponse update(UpdateFeedbackRequest request) {
        var feedback = getFeedbackById(request.feedbackId());

        feedback.updateFeedback(FeedbackRating.fromValue(request.rating()), request.toFeedbackCheckList(), request.description());

        return UpdateFeedbackResponse.from(feedback);
    }

    @Transactional
    public GetByFeedbackIdResponse getFeedbackByFeedbackId(Long feedbackId) {
        return GetByFeedbackIdResponse.of(getFeedbackById(feedbackId));
    }

    private Question getQuestionById(Long questionId) {
        return questionRepository
                .findById(questionId)
                .orElseThrow(QuestionNotFoundException::new);
    }

    private Feedback getFeedbackById(Long feedbackId) {
        return feedbackRepository
                .findById(feedbackId)
                .orElseThrow(() -> new FeedbackNotFoundException(feedbackId));
    }
}