package com.project.danim_be.member.dto.ResponseDto;

import com.project.danim_be.member.entity.Member;
import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter

public class LoginResponseDto {
	private final Long id;
	private SseEmitter sseEmitter;

	public LoginResponseDto(Member member, SseEmitter sseEmitter) {
		this.id = member.getId();
		this.sseEmitter = sseEmitter;
	}


}
