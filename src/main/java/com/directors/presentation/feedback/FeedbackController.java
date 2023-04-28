package com.directors.presentation.feedback;

import com.directors.application.feedback.FeedbackService;
import com.directors.presentation.feedback.request.CreateFeedbackRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create(CreateFeedbackRequest request, @AuthenticationPrincipal String userIdByToken) {
        feedbackService.create(request, userIdByToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
