package com.directors.domain.schedule;

public enum ScheduleStatus {
	OPENED("opend"), CLOSED("closed");
	private final String value;

	ScheduleStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
