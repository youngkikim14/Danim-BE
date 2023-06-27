package com.project.danim_be.member.dto.ResponseDto;

import com.project.danim_be.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {

	private Long id;

	// private SseEmitter sseEmitter;

	private String nickName;

	private String myPageImageUrl;

	private Boolean isExistMember;

	public LoginResponseDto(Member member, Boolean isExistMember) {
		this.id = member.getId();
		this.nickName = member.getNickname();
		this.myPageImageUrl = member.getImageUrl();
		// this.sseEmitter = sseEmitter;
		this.isExistMember = isExistMember;
	}

	public LoginResponseDto(Member member) {
		this.id = member.getId();
		this.nickName = member.getNickname();
		this.myPageImageUrl = member.getImageUrl();
		// this.sseEmitter = sseEmitter;
	}

}
