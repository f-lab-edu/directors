package com.directors.presentation.qeustion;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.directors.application.question.QuestionService;
import com.directors.presentation.qeustion.response.QuestionResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/question")
public class QuestionController {
	private final QuestionService questionService;

	@GetMapping("/sent")
	public ResponseEntity<List<QuestionResponseDto>> sendList(@RequestParam String questionerId) {
		List<QuestionResponseDto> questionDtos = questionService.getSendList(questionerId);
		return new ResponseEntity<>(questionDtos, HttpStatus.OK);
	}

	@GetMapping("/received")
	public ResponseEntity<List<QuestionResponseDto>> receiveList(@RequestParam String directorId) {
		List<QuestionResponseDto> questionDtos = questionService.getReceiveList(directorId);
		return new ResponseEntity<>(questionDtos, HttpStatus.OK);
	}
}
