package com.project.danim_be.member.dto;

import lombok.Getter;

@Getter
public class LoginRequestDto {

	private String userId;
	private String password;

	public LoginRequestDto(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}
}
