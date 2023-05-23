package com.project.danim_be.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoMemberInfoDto {

	private String email;
	private String gender;
	private String ageRange;

	public KakaoMemberInfoDto(String email, String gender, String ageRange) {
		this.email = email;
		this.gender=gender;
		this.ageRange=ageRange;
	}

}
