package com.project.danim_be.member.dto.RequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {

	private String userId;
	private String password;

	public LoginRequestDto(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}
}
