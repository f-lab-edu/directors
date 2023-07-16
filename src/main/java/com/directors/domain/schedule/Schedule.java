package com.directors.domain.schedule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.directors.domain.common.BaseEntity;
import com.directors.domain.question.Question;
import com.directors.domain.user.User;
import com.directors.infrastructure.exception.ExceptionCode;
import com.directors.infrastructure.exception.schedule.InvalidMeetingRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime startTime;
	@Enumerated(EnumType.STRING)
	private ScheduleStatus status;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Question> questionList = new ArrayList<>();

	@Builder
	public Schedule(LocalDateTime startTime, ScheduleStatus status, User user) {
		this.startTime = startTime;
		this.status = status;
		this.user = user;
	}

	public static Schedule of(LocalDateTime startTime, ScheduleStatus status, User user) {
		return Schedule.builder()
			.startTime(startTime)
			.status(status)
			.user(user)
			.build();
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public ScheduleStatus getStatus() {
		return status;
	}

	public User getUser() {
		return user;
	}

	public void checkChangeableScheduleTime() {
		if (this.status == ScheduleStatus.CLOSED) {
			throw new InvalidMeetingRequest(ExceptionCode.ClosedSchedule, startTime, user.getId());
		}
	}

	public boolean equalsStartTime(LocalDateTime startTime) {
		return this.startTime.equals(startTime);
	}

	public void openSchedule() {
		if (this.status == ScheduleStatus.CLOSED) {
			this.status = this.status.changStatus();
		}
	}

	public void closeSchedule() {
		if (this.status == ScheduleStatus.OPENED) {
			this.status = this.status.changStatus();
		}
	}

	public boolean isNewSchedule() {
		return this.id == null;
	}

}
