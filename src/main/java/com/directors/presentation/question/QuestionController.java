package com.directors.presentation.question;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.directors.application.question.QuestionService;
import com.directors.presentation.question.request.CreateQuestionRequest;
import com.directors.presentation.question.request.DeclineQuestionRequest;
import com.directors.presentation.question.request.EditQuestionRequest;
import com.directors.presentation.question.response.DetailQuestionResponse;
import com.directors.presentation.question.response.ReceivedQuestionResponse;
import com.directors.presentation.question.response.SentQuestionResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/question")
public class QuestionController {
	private final QuestionService questionService;

	@GetMapping("/sent")
	public ResponseEntity<List<SentQuestionResponse>> sendList(@AuthenticationPrincipal String userIdByToken) {
		List<SentQuestionResponse> questionDtos = questionService.getSendList(userIdByToken);
		return new ResponseEntity<>(questionDtos, HttpStatus.OK);
	}

	@GetMapping("/received")
	public ResponseEntity<List<ReceivedQuestionResponse>> receiveList(@AuthenticationPrincipal String userIdByToken) {
		List<ReceivedQuestionResponse> questionDtos = questionService.getReceiveList(userIdByToken);
		return new ResponseEntity<>(questionDtos, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<HttpStatus> createQuestion(@AuthenticationPrincipal String userIdByToken,
		@RequestBody @Valid CreateQuestionRequest createQuestionRequest) {

		questionService.create(createQuestionRequest, userIdByToken);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/{questionId}")
	public ResponseEntity<?> editQuestion(@PathVariable Long questionId,
		@RequestBody @Valid EditQuestionRequest editQuestionRequest) {
		questionService.edit(questionId, editQuestionRequest);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/{questionId}")
	public ResponseEntity<?> getQuestionDetail(@PathVariable Long questionId) {
		DetailQuestionResponse questionDetail = questionService.getQuestionDetail(questionId);
		return new ResponseEntity<>(questionDetail, HttpStatus.OK);
	}

	@PostMapping("/{questionId}/decline")
	public ResponseEntity<?> declineQuestion(@PathVariable Long questionId,
		@AuthenticationPrincipal String userIdByToken,
		@RequestBody @Valid DeclineQuestionRequest declineQuestionRequest) {

		questionService.decline(questionId, userIdByToken, declineQuestionRequest);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/{questionId}/accept")
	public ResponseEntity<?> acceptQuestion(@PathVariable Long questionId,
		@AuthenticationPrincipal String userIdByToken) {
		questionService.accept(questionId, userIdByToken);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("{questionId}/finish")
	public ResponseEntity<?> finishQuestion(@PathVariable Long questionId,
		@AuthenticationPrincipal String userIdByToken) {
		questionService.complete(questionId, userIdByToken);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
