package com.directors.presentation.question;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.directors.application.question.QuestionService;
import com.directors.presentation.question.request.CreateQuestionRequest;
import com.directors.presentation.question.request.EditQuestionRequest;
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
	private final String loginId = "dummyUser";

	@GetMapping("/sent")
	public ResponseEntity<List<SentQuestionResponse>> sendList(String loginUserId) {
		List<SentQuestionResponse> questionDtos = questionService.getSendList(loginId);
		return new ResponseEntity<>(questionDtos, HttpStatus.OK);
	}

	@GetMapping("/received")
	public ResponseEntity<List<ReceivedQuestionResponse>> receiveList(String loginUserId) {
		List<ReceivedQuestionResponse> questionDtos = questionService.getReceiveList(loginId);
		return new ResponseEntity<>(questionDtos, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<HttpStatus> createQuestion(String loginUserId,
		@RequestBody @Valid CreateQuestionRequest createQuestionRequest) {

		questionService.create(createQuestionRequest, loginId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/{questionId}")
	public ResponseEntity<?> editQuestion(@PathVariable Long questionId,
		@RequestBody @Valid EditQuestionRequest editQuestionRequest) {
		questionService.edit(questionId, editQuestionRequest);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
