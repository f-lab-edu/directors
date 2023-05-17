package com.directors.infrastructure.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.directors.domain.feedback.exception.CannotCreateFeedbackException;
import com.directors.domain.feedback.exception.FeedbackNotFoundException;
import com.directors.domain.region.exception.RegionNotFoundException;
import com.directors.domain.room.exception.CannotCreateRoomException;
import com.directors.domain.room.exception.RoomNotFoundException;
import com.directors.domain.specialty.exception.NoSuchSpecialtyException;
import com.directors.domain.user.exception.AuthenticationFailedException;
import com.directors.domain.user.exception.DuplicateIdException;
import com.directors.domain.user.exception.NoSuchUserException;
import com.directors.domain.user.exception.NotEnoughRewardException;
import com.directors.domain.user.exception.UserRegionNotFoundException;
import com.directors.infrastructure.exception.api.RenewApiKeyException;
import com.directors.infrastructure.exception.common.EntityNotFoundException;
import com.directors.infrastructure.exception.question.CannotDecideQuestionException;
import com.directors.infrastructure.exception.question.InvalidQuestionStatusException;
import com.directors.infrastructure.exception.question.QuestionDuplicateException;
import com.directors.infrastructure.exception.question.QuestionNotFoundException;
import com.directors.infrastructure.exception.schedule.ClosedScheduleException;
import com.directors.infrastructure.exception.schedule.InvalidChangeScheduleException;
import com.directors.infrastructure.exception.schedule.InvalidMeetingRequest;
import com.directors.infrastructure.exception.schedule.InvalidMeetingTimeException;

import io.jsonwebtoken.JwtException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.ConnectException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * 상속한 ResponseEntityExceptionHandler 클래스에 handleMethodArgumentNotValid 메소드가 있기 때문에
	 * 이를 오버라이드하여 구현했습니다.
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		BindingResult bindingResult = e.getBindingResult();
		return new ResponseEntity<>(new ErrorMessage(bindingResult.getFieldErrors().get(0).getDefaultMessage()),
			HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(DuplicateIdException.class)
	public ErrorMessage DuplicateIdExceptionHandler(DuplicateIdException e) {
		log.info("DuplicateIdException occurred. duplicatedId: " + e.duplicatedId);
		return new ErrorMessage(e.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoSuchUserException.class)
	public ErrorMessage NosuchUserExceptionHandler(NoSuchUserException e) {
		log.info("NosuchUserException occurred. requested userId: " + e.requestedUserId);
		return new ErrorMessage(e.getMessage());
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(AuthenticationFailedException.class)
	public ErrorMessage AuthenticationFailedExceptionHandler(AuthenticationFailedException e) {
		log.info("AuthenticationFailedException occurred. requested userId:" + e.requestedUserId);
		return new ErrorMessage(e.getMessage());
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(JwtException.class)
	public ErrorMessage JwtExceptionHandler() {
		log.info("JwtException occurred.");
		return new ErrorMessage("유효하지 않은 토큰입니다.");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<HttpStatus> illegalArgumentExceptionHandler() {
		log.info("IllegalArgumentException occurred.");
		return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ClosedScheduleException.class)
	public ErrorMessage closedScheduleExceptionHandler(ClosedScheduleException ex) {
		log.info(String.format("ClosedScheduleException occurred. time = %s, userId = %s", ex.getStartTime(),
			ex.getUserId()));
		return new ErrorMessage(ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidMeetingTimeException.class)
	public ErrorMessage invalidMeetingTimeException(InvalidMeetingTimeException ex) {
		log.info("InvalidMeetingTimeException occurred. userId = {} startTIme = {} ", ex.getUserId(),
			ex.getStartTime());
		return new ErrorMessage(ex.getMessage());
	}

	@ExceptionHandler(InvalidMeetingRequest.class)
	public ResponseEntity<?> invalidMeetingException(InvalidMeetingRequest ex) {
		log.info("{} occurred, userId = {}, startTime = {}", ex.getMessage(), ex.getUserId(),
			ex.getStartTime());
		return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), ex.getStatusCode());
	}

	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ExceptionHandler(HttpClientErrorException.class)
	public ErrorMessage httpClientErrorException(HttpClientErrorException e) {
		String errorMessage = "HttpClientErrorException occurred. " + e.getStatusCode();
		if (e.getStatusCode().equals("412 PRECONDITION_FAILED")) {
			errorMessage += "need to check api request parameter.";
		}

		log.warn(errorMessage);
		return new ErrorMessage("잠시 후 다시 시도해주세요");
	}

	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ExceptionHandler(HttpServerErrorException.class)
	public ErrorMessage httpServerErrorException(HttpServerErrorException e) {
		log.error("HttpServerErrorException occurred. " + e.getStatusCode());
		return new ErrorMessage("잠시 후 다시 시도해주세요");
	}

	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ExceptionHandler(RenewApiKeyException.class)
	public ErrorMessage renewApiKeyException() {
		log.error("RenewApiKeyException occurred. API key renewal is required.");
		return new ErrorMessage("잠시 후 다시 시도해주세요");
	}

	@ExceptionHandler(QuestionNotFoundException.class)
	public ResponseEntity<?> questionNotFoundException(QuestionNotFoundException ex) {
		log.info("QuestionNotFoundException. questionId = {}", ex.getQuestionId());
		return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), ex.getStatusCode());
	}

	@ExceptionHandler(InvalidQuestionStatusException.class)
	public ResponseEntity<?> invalidQuestionStatusException(InvalidQuestionStatusException ex) {
		log.info("InvalidQuestionStatusException occurred. questionId = {}, questionStatus = {}", ex.getQuestionId(),
			ex.getQuestionStatus());
		return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), ex.getStatusCode());
	}

	@ExceptionHandler(QuestionDuplicateException.class)
	public ResponseEntity<?> questionDuplicateException(QuestionDuplicateException ex) {
		log.info("questionDuplicateException occurred. questionerId = {}", ex.getQuestionerId());
		return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), ex.getStatusCode());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoSuchSpecialtyException.class)
	public ErrorMessage noSuchSpecialtyException(NoSuchSpecialtyException e) {
		log.warn("NoSuchSpecialtyException occurred.");
		return new ErrorMessage(e.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(UserRegionNotFoundException.class)
	public ErrorMessage userRegionNotFoundException(UserRegionNotFoundException e) {
		log.warn("UserRegionNotFoundException occurred. requestedUserId: " + e.requestedUserId);
		return new ErrorMessage(e.getMessage());
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(CannotCreateRoomException.class)
	public ErrorMessage cannotCreateRoomException(CannotCreateRoomException e) {
		log.info("CannotCreateRoomException occurred. questionId = " + e.questionId);
		return new ErrorMessage(e.getMessage());
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(RoomNotFoundException.class)
	public ErrorMessage roomNotFoundException(RoomNotFoundException e) {
		log.info("RoomNotFoundException occurred. " + "roomId = " + e.getRoomId() +
			", requestUserId = " + e.getRequestUserId());
		return new ErrorMessage(e.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(RegionNotFoundException.class)
	public ErrorMessage regionNotFoundException(RegionNotFoundException e) {
		log.info("RegionNotFoundException occurred. " + "fullAddress = " + e.getFullAddress());
		return new ErrorMessage(e.getMessage());
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(CannotCreateFeedbackException.class)
	public ErrorMessage cannotCreateFeedbackException(CannotCreateFeedbackException e) {
		log.info("CannotCreateFeedbackException occurred. questionId = " + e.questionId);
		return new ErrorMessage(e.getMessage());
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(FeedbackNotFoundException.class)
	public ErrorMessage feedbackNotFoundException(FeedbackNotFoundException e) {
		log.info("FeedbackNotFoundException occurred. " + "feedbackId = " + e.getFeedbackId());
		return new ErrorMessage(e.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(EntityNotFoundException.class)
	public ErrorMessage entityNotFoundException(EntityNotFoundException e) {
		log.info("EntityNotFoundException occurred.");
		return new ErrorMessage(e.getMessage());
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(InvalidChangeScheduleException.class)
	public ErrorMessage invalidChangeScheduleException(InvalidChangeScheduleException e) {
		log.info("Invalid Changeable Schedule Status, startTime = {}, Status = {}", e.getStartTime(), e.getStatus());
		return new ErrorMessage(e.getMessage());
	}

	@ExceptionHandler(CannotDecideQuestionException.class)
	public ResponseEntity<?> cannotDecideQuestionException(CannotDecideQuestionException e) {
		log.info("Can not decide question exception, questionId = {}", e.questionId);
		return new ResponseEntity<>(new ErrorMessage(e.getMessage()), e.getStatusCode());
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(NotEnoughRewardException.class)
	public ErrorMessage notEnoughRewardException(NotEnoughRewardException e) {
		log.info("Not Enough Reward Exception, userId = {}", e.userId);
		return new ErrorMessage(e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	@ExceptionHandler(ConnectException.class)
	public ErrorMessage connectException(CannotCreateRoomException e) {
		log.info("ConnectException occurred. Please check redis connection.");
		return new ErrorMessage("잠시 후 다시 시도해주세요");
	}
}

@Getter
class ErrorMessage {
	private final String message;

	ErrorMessage(String message) {
		this.message = message;
	}
}