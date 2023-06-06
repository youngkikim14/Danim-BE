package com.project.danim_be.common.exception;

import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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
	NOT_CONTAIN_AGERANGE(HttpStatus.UNAUTHORIZED,"신청하신 나이대에 포함되지않습니다.","모임 신청 오류입니다."),

	SENDER_MISMATCH(HttpStatus.NOT_FOUND,"메시지를 보낸사람의 닉네임이 일치하지않습니다." ,"잘못된 접근입니다." ),
	ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 채팅방 입니다.","채팅방 접근오류 입니다."),
	//Forbidden
	DO_NOT_HAVE_PERMISSION(HttpStatus.FORBIDDEN, "자신만 수정할수 있습니다", "권한이 없는 사용자입니다"),
	NOT_MOD_AUTHORIZED_MEMBER(HttpStatus.FORBIDDEN,"글작성자만 수정할 수 있습니다." ,"권한이 없는 사용자입니다"),
	NOT_DEL_AUTHORIZED_MEMBER(HttpStatus.FORBIDDEN,"글작성자만 삭제할 수 있습니다." ,"권한이 없는 사용자입니다"),
	FAIL_CONNECTION(HttpStatus.INTERNAL_SERVER_ERROR, "알림 연결을 실패했습니다", "알림 관련 오류"),
	FAIL_SEND_NOTIFICATION(HttpStatus.INTERNAL_SERVER_ERROR, "이벤트 알림 전송 실패", "알림 관련 오류"),
	FAIL_FIND_MEMBER_CHAT_ROOM(HttpStatus.NOT_FOUND, "이전에 접속했던 채팅방 정보를 찾을 수 없습니다.", "문제가 지속될 경우 관리자에게 문의해주세요."),
	NOT_ADMIN_ACCESS(HttpStatus.UNAUTHORIZED,"권한이 있는 사용자만 할수있습니다.","권한이 없는 사용자입니다."),
	USER_KICKED(HttpStatus.UNAUTHORIZED,"이미 강퇴당한 방입니다" ,"권한이 없는 사용자입니다.");


	private final HttpStatus httpStatus;
	private final String detail;
	private final String message;
}
