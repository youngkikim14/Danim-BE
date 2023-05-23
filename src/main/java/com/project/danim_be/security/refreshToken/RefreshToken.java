package com.project.danim_be.security.refreshToken;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotBlank
	private String refreshToken;

	@NotBlank
	private String userId;

	public RefreshToken(String tokenDto,  String userId) {
		this.refreshToken = tokenDto;
		this.userId = userId;
	}

	public RefreshToken updateToken(String tokenDto) {
		this.refreshToken = tokenDto;
		return this;
	}
}
