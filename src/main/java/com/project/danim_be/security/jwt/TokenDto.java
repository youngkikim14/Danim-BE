package com.project.danim_be.security.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenDto {

	private String accessToken;
	private String refreshToken;
	private Long accessTokenExpiration;
	private Long refreshTokenExpiration;


	public TokenDto(String accessToken, String refreshToken, Long accessTokenExpiration, Long refreshTokenExpiration) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

}