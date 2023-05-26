package com.project.danim_be.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String userId;		//아이디

	@Column(nullable = false)
	private String password;	//비밀번호

	private String nickname;	//닉네임

	// @Column(nullable = false)
	private String ageRange;	//연령대

	private String provider;	//소셜

	private String imageUrl;	//프로필이미지

	private String gender;		//성별

	private String content;		//(간략한)자기소개

	private Boolean isDeleted;		//탈퇴 여부


	public Member(String userId, String password, String nickname) {
		this.userId = userId;
		this.password = password;
		this.nickname = nickname;
		this.ageRange = "don't receive";
		this.provider = "google";
  }


	public Member(String userId, String password, String ageRange,String nickname) {
		this.userId = userId;
		this.nickname = nickname;
		this.password = password;
		this.ageRange = ageRange;
	}

	@Builder
	public Member(String userId, String gender, String password, String nickname, String ageRange, String provider, Boolean isDeleted) {
		this.userId = userId;
		this.gender = gender;
		this.password = password;
		this.nickname = nickname;
		this.ageRange = ageRange;
		this.provider = provider;
		this.isDeleted = isDeleted;
	}

	public String getUserId() {
		return userId;
	}

	public void signOut() {
		this.userId = "탈퇴";
		this.gender = "탈퇴";
		this.nickname = null;
		this.ageRange = "탈퇴";
		this.isDeleted = true;
	}
}
