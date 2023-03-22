package com.directors.presentation.qeustion;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.directors.application.question.QuestionService;
import com.directors.presentation.qeustion.response.ReceivedQuestionResponse;
import com.directors.presentation.qeustion.response.SentQuestionResponse;

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
}
