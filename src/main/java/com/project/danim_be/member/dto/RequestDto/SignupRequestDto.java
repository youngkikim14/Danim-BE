package com.project.danim_be.member.dto.RequestDto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {

//	   @Pattern(regexp = "^[a-z0-9_+.-]+@[a-z0-9-]+\\.[a-z0-9]{1,20}$", message = "아이디는 올바른 이메일 형식으로 입력해주세요. (ex-danim999@naver.com)")
	 private String userId;

//	   @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d!@#$%^&*()_-]{5,12}$", message = "비밀번호는 5~12자 이내 영어(소문자),숫자,특수기호(선택) 범위에서 입력해야합니다.")
	 private String password;

//	   @Pattern(regexp ="^(?=.*[가-힣a-zA-Z])[가-힣a-zA-Z0-9]{3,8}$", message = "닉네임은 3~8자 이내 한글or영어(대소문자),숫자(선택) 범위에서 입력해주세요. 특수문자는 포함할 수 없습니다.")
	 private String nickname;

	 private String ageRange;
	 @Enumerated(EnumType.STRING)
	 private String gender;

	 // @JsonProperty("AgreeForGender")
	 private Boolean agreeForGender;

	 // @JsonProperty("AgreeForAge")
	 private Boolean agreeForAge;

}
