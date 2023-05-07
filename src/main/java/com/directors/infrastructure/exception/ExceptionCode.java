package com.directors.infrastructure.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionCode {
	ClosedSchedule("동일한 시각에 예약한 사람이 있습니다", HttpStatus.CONFLICT),
	InvalidMeetingTime("디렉터가 허용한 만남시간이 아닙니다.", HttpStatus.FORBIDDEN),
	QuestionNotFound("존재하지 않는 질문입니다.", HttpStatus.NOT_FOUND),
	InvalidQuestionStatus("대기 상태의 질문만 수정 가능합니다.", HttpStatus.FORBIDDEN),
	QuestionDuplicated("동일한 디렉터와 진행중인 질문이 존재합니다. 질문을 수정하거나 삭제 후 다시 시도해주세요.", HttpStatus.CONFLICT),
	InvalidStartTime("약속 시간은 현재 시간보다 이후 시간대여야 합니다.", HttpStatus.CONFLICT),
	InvalidFinishQuestionStatus("진행중인 질문만 약속 이행 여부를 체크할 수 있습니다.", HttpStatus.FORBIDDEN),
	InvalidDecideQuestion("질문을 수락 및 거절 할 수있는 권한이 없습니다.", HttpStatus.CONFLICT),
	InvalidFinishQuestion("약속 이행을 체크할 권한이 없습니다.", HttpStatus.CONFLICT);

	private final String message;
	private final HttpStatus status;

	ExceptionCode(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return this.message;
	}

	public HttpStatus getStatus() {
		return this.status;
	}

}