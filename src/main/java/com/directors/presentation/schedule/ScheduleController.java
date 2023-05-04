package com.directors.presentation.schedule;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.directors.application.schedule.ScheduleService;
import com.directors.presentation.schedule.request.CreateScheduleRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

	private final ScheduleService scheduleService;

	@PutMapping
	ResponseEntity<?> open(@AuthenticationPrincipal String userIdByToken,
		@RequestBody CreateScheduleRequest createScheduleRequest) {
		scheduleService.open(userIdByToken, createScheduleRequest.getStartTimeList());
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
