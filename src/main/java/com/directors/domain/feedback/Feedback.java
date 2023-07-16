package com.directors.domain.feedback;

import com.directors.domain.common.BaseEntity;
import com.directors.domain.question.Question;
import com.directors.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "feedback")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FeedbackRating feedbackRating;

    @ElementCollection(targetClass = FeedbackCheck.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "feedback_check", joinColumns = @JoinColumn(name = "feedback_id"))
    private List<FeedbackCheck> feedbackCheckList = new ArrayList<>();

    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_Id")
    private Question question;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "director_id", referencedColumnName = "id")
    private User director;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "questioner_id", referencedColumnName = "id")
    private User questioner;

    @Builder
    public Feedback(FeedbackRating feedbackRating, List<FeedbackCheck> feedbackCheckList, String description, Question question, User director, User questioner) {
        this.feedbackRating = feedbackRating;
        this.feedbackCheckList = feedbackCheckList;
        this.description = description;
        this.question = question;
        this.director = director;
        this.questioner = questioner;
    }

    public Long getId() {
        return id;
    }

    public FeedbackRating getFeedbackRating() {
        return feedbackRating;
    }

    public String getDescription() {
        return description;
    }

    public Question getQuestion() {
        return question;
    }

    public User getDirector() {
        return director;
    }

    public User getQuestioner() {
        return questioner;
    }

    public List<String> getFeedbackCheckValues() {
        return feedbackCheckList.stream()
                .map(FeedbackCheck::getValue)
                .collect(Collectors.toList());
    }

    public void updateFeedback(FeedbackRating rating, List<FeedbackCheck> checkedList, String description) {
        this.feedbackRating = rating;
        this.feedbackCheckList = checkedList;
        this.description = description;
    }
}
