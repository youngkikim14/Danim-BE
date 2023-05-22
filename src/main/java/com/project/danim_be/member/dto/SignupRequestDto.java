package com.project.danim_be.member.dto;

import org.thymeleaf.spring6.processor.SpringInputGeneralFieldTagProcessor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {


	@Pattern(regexp = "^[a-z0-9_+.-]+@[a-z0-9-]+\\.[a-z0-9]{2,4}$", message = "아이디는 올바른 이메일 형식으로 입력해주세요.")
	private String userId;

	@Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d!@#$%^&*()_-]{5,12}$", message = "비밀번호정규식")
	private String password;

	@Pattern(regexp ="^(?=.*[가-힣a-zA-Z])[가-힣a-zA-Z0-9]{3,8}$",message="닉네임정규식")
	private String nickname;

	private String ageRange;




}
