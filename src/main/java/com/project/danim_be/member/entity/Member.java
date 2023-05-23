package com.project.danim_be.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String userId;		//아이디

	@Column(nullable = false)
	private String password;	//비밀번호

	@Column(nullable = false)
	private String nickname;	//닉네임

	@Column(nullable = false)
	private String ageRange;	//연령대

	private String provider;	//소셜

	private String imageUrl;	//프로필이미지

	private String gender;		//성별

	private String content;		//(간략한)자기소개
}
