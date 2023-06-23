package com.project.danim_be.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvisor {


	// CustomException 클래스에서 발생하는 예외 핸들러
	@ExceptionHandler(value = {CustomException.class})
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();
		return ErrorResponse.toResponseEntity(errorCode);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException exc) {
		exc.getMaxUploadSize();
		exc.getMessage();
		String errorMessage = "업로드하려는 파일의 크기가 10MB를 초과했습니다.";
		return ResponseEntity
			.status(HttpStatus.EXPECTATION_FAILED)
			.body(new ErrorResponse("FILE_CAPACITY_ERROR",HttpStatus.BAD_REQUEST.value(), errorMessage));
	}
	// Valid 예외 핸들러
	@ExceptionHandler(value = {BindException.class})
	public ResponseEntity<ErrorResponse> handleBindException(BindException  ex) {
		BindingResult bindingResult = ex.getBindingResult();

		StringBuilder sb = new StringBuilder();
		for ( FieldError fieldError : bindingResult.getFieldErrors()) {
			sb.append(fieldError.getDefaultMessage());
		}
		return ErrorResponse.toResponseEntityValid(sb.toString(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e){
		e.printStackTrace();
		return ErrorResponse.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
	}

	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e){
		e.printStackTrace();
		return ErrorResponse.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
	}
	
	@ExceptionHandler(value = NullPointerException.class)
	public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e){
		e.printStackTrace();
		return ErrorResponse.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
	}

}
