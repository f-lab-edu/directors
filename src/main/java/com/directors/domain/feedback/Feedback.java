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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
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

    public void updateFeedback(FeedbackRating rating, List<FeedbackCheck> checkedList, String description) {
        this.feedbackRating = rating;
        this.feedbackCheckList = checkedList;
        this.description = description;
    }

    public List<String> getFeedbackCheckValues() {
        return feedbackCheckList.stream()
                .map(FeedbackCheck::getValue)
                .collect(Collectors.toList());
    }
}
