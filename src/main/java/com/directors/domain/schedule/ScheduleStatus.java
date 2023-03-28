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
}
