package com.directors.presentation.feedback;

import com.directors.application.feedback.FeedbackService;
import com.directors.presentation.feedback.request.CreateFeedbackRequest;
import com.directors.presentation.feedback.request.UpdateFeedbackRequest;
import com.directors.presentation.feedback.response.CreateFeedbackResponse;
import com.directors.presentation.feedback.response.GetByFeedbackIdResponse;
import com.directors.presentation.feedback.response.UpdateFeedbackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("/create")
    public ResponseEntity<CreateFeedbackResponse> create(CreateFeedbackRequest request, @AuthenticationPrincipal String userIdByToken) {
        return new ResponseEntity<>(feedbackService.create(request, userIdByToken), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UpdateFeedbackResponse> update(UpdateFeedbackRequest request) {
        return new ResponseEntity<>(feedbackService.update(request), HttpStatus.OK);
    }

    @GetMapping("/{feedbackId}")
    public ResponseEntity<GetByFeedbackIdResponse> getByFeedbackId(@PathVariable Long feedbackId) {
        var feedback = feedbackService.getFeedbackByFeedbackId(feedbackId);
        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }
}
