package com.gptsam.core.exception.handler;

import com.gptsam.core.common.response.ApiResponse;
import com.gptsam.core.exception.type.InternalServerException;
import com.gptsam.core.exception.type.biz.BadRequestException;
import com.gptsam.core.exception.type.biz.ForbiddenException;
import com.gptsam.core.exception.type.biz.NotFoundException;
import com.gptsam.core.exception.type.biz.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@Value("${spring.application.name}")
	private String appName;


	/**
	 * 400 Bad Request<br>
	 * 사용자 정의 예외를 처리하는 핸들러 메소드
	 */
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException e) {
		return createErrorResponse(e, HttpStatus.BAD_REQUEST, e.getMessage());
	}

	/**
	 * 401 Unauthorized<br>
	 * 인증되지 않은 사용자가 인증이 필요한 항목에 접근한 경우의 예외를 처리하는 핸들러 메소드
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ApiResponse<Void>> handleUnauthorized(UnauthorizedException e) {
		return createErrorResponse(e, HttpStatus.UNAUTHORIZED, e.getMessage());
	}

	/**
	 * 403 Forbidden<br>
	 * 요청이 권한을 벗어났을 경우의 예외를 처리하는 핸들러 메소드
	 */
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ApiResponse<Void>> handleForbidden(ForbiddenException e) {
		return createErrorResponse(e, HttpStatus.FORBIDDEN, e.getMessage());
	}

	/**
	 * 404 Not Found<br>
	 * 요청한 정적 리소스를 찾지 못했을 경우의 예외를 처리하는 핸들러 메소드
	 */
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleNotFound(NotFoundException e) {
		return createErrorResponse(e, HttpStatus.NOT_FOUND, e.getMessage());
	}

	/**
	 * 404 Not Found<br>
	 * 요청한 콘텐츠를 찾지 못했을 경우의 예외를 처리하는 핸들러 메소드
	 */
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleNoResourceFound(NoResourceFoundException e) {
		return createErrorResponse(e, HttpStatus.NOT_FOUND, e.getMessage());
	}

	/**
	 * 500 Internal Server Error<br>
	 * 예외 처리를 했지만, 임시로 분류된 예외를 처리하는 핸들러 메소드
	 */
	@ExceptionHandler(InternalServerException.class)
	public ResponseEntity<ApiResponse<Void>> handleInternalServerException(InternalServerException e) {
		return createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

	/**
	 * 500 Internal Server Error<br>
	 * 예외 처리를 하지 못한 나머지 예외를 처리하는 핸들러 메소드
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception e) {
		return createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}


	private ResponseEntity<ApiResponse<Void>> createErrorResponse(Exception e,
																  HttpStatus status,
																  String message) {
		logMessage(e, status);
		return ResponseEntity.status(status)
							 .body(ApiResponse.error(message));
	}

	private void logMessage(Exception e, HttpStatus status) {
		if (status.is4xxClientError()) {
			// 비즈니스 예외 - WARN
			log.warn("[{}] {}: {}",
					 status.getReasonPhrase(),
					 e.getClass().getSimpleName(),
					 e.getMessage());
			return;
		}
		if (status.is5xxServerError()) {
			if (e instanceof InternalServerException) {
				// 분류되지 않은 예외 - WARN
				log.warn("[{} Exception] {}: {}",
						 appName,
						 e.getClass().getSimpleName(),
						 e.getMessage(),
						 e);
			} else {
				// 예상하지 못한 예외 - ERROR
				log.error("[Unknown Exception] {}: {}",
						  e.getClass().getSimpleName(),
						  e.getMessage(),
						  e);
			}
		}
	}

}
