package com.directors.presentation.qeustion;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/sent/{questionerId}")
	public ResponseEntity<List<SentQuestionResponse>> sendList(@PathVariable String questionerId) {
		List<SentQuestionResponse> questionDtos = questionService.getSendList(questionerId);
		return new ResponseEntity<>(questionDtos, HttpStatus.OK);
	}

	@GetMapping("/received/{directorId}")
	public ResponseEntity<List<ReceivedQuestionResponse>> receiveList(@PathVariable String directorId) {
		List<ReceivedQuestionResponse> questionDtos = questionService.getReceiveList(directorId);
		return new ResponseEntity<>(questionDtos, HttpStatus.OK);
	}
}
