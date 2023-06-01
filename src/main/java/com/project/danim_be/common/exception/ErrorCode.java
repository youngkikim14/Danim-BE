package com.project.danim_be.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	//400
	DUPLICATE_IDENTIFIER(HttpStatus.BAD_REQUEST,"중복된 아이디 입니다.","사용자 등록 오류입니다."),
	DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST,"중복된 닉네임 입니다.","사용자 등록 오류입니다."),
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST,"잘못된 비밀번호 입니다.","사용자 등록 오류입니다."),
	DELETED_USER(HttpStatus.BAD_REQUEST, "탈퇴한 사용자입니다.", "사용자 등록 오류입니다."),
	FAIL_SIGNOUT(HttpStatus.BAD_REQUEST, "탈퇴에 실패했습니다.", "사용자 등록 오류입니다."),

	FILE_CONVERT_FAIL(HttpStatus.BAD_REQUEST,"파일 변환에 실패했습니다","파일 등록 오류입니다"),

	CANNOT_WRITE_REVIEW(HttpStatus.BAD_REQUEST, "여행이 종료되어야 작성할 수 있습니다.", "사용자 등록 오류입니다."),
	//404
	ID_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 아이디 입니다.","사용자 등록 오류입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을수 없는 회원입니다.","사용자 등록 오류입니다."),
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.", "사용자 등록 오류입니다."),
	//Forbidden
	DO_NOT_HAVE_PERMISSION(HttpStatus.FORBIDDEN, "자신만 수정할수 있습니다", "권한이 없는 사용자입니다"),
	NOT_MOD_AUTHORIZED_MEMBER(HttpStatus.FORBIDDEN,"글작성자만 수정할 수 있습니다." ,"권한이 없는 사용자입니다"),
	NOT_DEL_AUTHORIZED_MEMBER(HttpStatus.FORBIDDEN,"글작성자만 삭제할 수 있습니다." ,"권한이 없는 사용자입니다");

	private final HttpStatus httpStatus;
	private final String detail;
	private final String message;
}
