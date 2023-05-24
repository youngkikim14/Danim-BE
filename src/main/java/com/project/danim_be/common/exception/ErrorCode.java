package com.project.danim_be.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {


	DUPLICATE_IDENTIFIER(HttpStatus.BAD_REQUEST,"중복된 아이디 입니다","사용자 등록 오류입니다."),
	DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST,"중복된 닉네임 입니다","사용자 등록 오류입니다."),
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST,"잘못된 비밀번호 입니다","사용자 등록 오류입니다."),
	//404
	ID_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 아이디 입니다.","사용자 등록 오류입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을수 없는 회원입니다.","사용자 등록 오류입니다.");

	private final HttpStatus httpStatus;
	private final String detail;
	private final String message;
}
