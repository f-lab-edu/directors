package com.directors.domain.schedule;

public enum ScheduleStatus {
	OPENED("opened"), CLOSED("closed");
	private final String value;

	ScheduleStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public ScheduleStatus changStatus() {
		switch (this) {
			case OPENED -> {
				return CLOSED;
			}
			case CLOSED -> {
				return OPENED;
			}
			default -> throw new IllegalStateException("올바르지 않은 스케쥴 상태입니다.");
		}
	}
}
