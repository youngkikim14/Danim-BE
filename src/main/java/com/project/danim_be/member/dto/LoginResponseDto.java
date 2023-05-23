package com.project.danim_be.member.dto;

import com.project.danim_be.member.entity.Member;

import lombok.Getter;

@Getter
public class LoginResponseDto {
	private final Long id;

	public LoginResponseDto(Member member) {
		this.id = member.getId();
	}


}
