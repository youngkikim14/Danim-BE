package com.project.danim_be.security.refreshToken;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Setter
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String refreshToken;

	@NotBlank
	private String userId;

	@NotBlank
	private String provider;

	public RefreshToken(String tokenDto, String userId, String provider) {
		this.refreshToken = tokenDto;
		this.userId = userId;
		this.provider = provider;
	}

	public RefreshToken(String refreshToken, String userId) {
		this.refreshToken = refreshToken;
		this.userId = userId;
		this.provider = "google";
	}

	public RefreshToken updateToken(String tokenDto) {
		this.refreshToken = tokenDto;
		return this;
	}
}
