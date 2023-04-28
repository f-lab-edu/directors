package com.directors.domain.feedback;

import com.directors.domain.common.BaseEntity;
import com.directors.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    //    TODO: 04.28 Question JPA 적용 시 변경
    //    @OneToOne(fetch = FetchType.EAGER)
    //    @JoinColumn(name ="question_Id")
    //    private Question question;
    private String questionId;

    public List<FeedbackCheck> getFeedbackCheckList() {
        if (feedbackCheckList == null) {
            feedbackCheckList = new ArrayList<>();
        }
        return feedbackCheckList;
    }

    public void setFeedbackCheckList(List<FeedbackCheck> feedbackCheckList) {
        this.feedbackCheckList = feedbackCheckList;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "director_id", referencedColumnName = "id")
    private User director;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "questioner_id", referencedColumnName = "id")
    private User questioner;
}
