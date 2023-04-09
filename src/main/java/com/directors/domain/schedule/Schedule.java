package com.directors.domain.schedule;

import java.time.LocalDateTime;

import com.directors.infrastructure.exception.ExceptionCode;
import com.directors.infrastructure.exception.schedule.InvalidMeetingRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Schedule {
	private Long scheduleId;
	private LocalDateTime startTime;
	private ScheduleStatus status;
	private String userId;

	public static Schedule of(Long id, LocalDateTime startTime, ScheduleStatus status, String userId) {
		return Schedule.builder()
			.scheduleId(id)
			.startTime(startTime)
			.status(status)
			.userId(userId)
			.build();
	}

	public void checkChangeableScheduleTime() {
		if (this.status == ScheduleStatus.CLOSED) {
			throw new InvalidMeetingRequest(ExceptionCode.ClosedSchedule, startTime, userId);
		}
	}
}
