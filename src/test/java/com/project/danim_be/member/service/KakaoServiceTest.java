package com.project.danim_be.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.project.danim_be.member.dto.KakaoMemberInfoDto;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;
import com.project.danim_be.security.refreshToken.RefreshToken;
import com.project.danim_be.security.refreshToken.RefreshTokenRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest

class KakaoServiceTest {
	@Mock
	private MemberRepository memberRepository;
	@InjectMocks
	private KakaoService kakaoService;
	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	@BeforeEach
	void signup(){
		// KakaoMemberInfoDto kakaoMemberInfoDto= new KakaoMemberInfoDto(
		// 	"test@kakao.com",
		// 	"male",
		// 	"30~39"
		// );

	}


	@Nested
	@DisplayName("카카오 회원가입테스트")
	class kakaoSignup{


		@Test
		@DisplayName("회원가입 성공 CASE -새로운사용자 생성 ")
		public void SignupTest(){

			//given

			String userId = "test@kakao.com";
			String gender = "male";
			String ageRange = "30-39";

			// String nickname = RandomNickname.getRandomNickname();
			// String password = UUID.randomUUID().toString();
			KakaoMemberInfoDto kakaoMemberInfoDto= new KakaoMemberInfoDto(userId,gender,ageRange);

			//when
			when(memberRepository.findByUserId(userId)).thenReturn(Optional.empty());

			Member member = kakaoService.kakaoSignup(kakaoMemberInfoDto);

			assertEquals(userId, member.getUserId());
			assertEquals(gender, member.getGender());
			assertEquals(ageRange, member.getAgeRange());
		}

		// @Test
		// @DisplayName("카카오 로그아웃 성공 테스트")
		// void kakaoSignoutSuccessTest() throws IOException {
		// 	// given
		// 	String userId = "test@kakao.com";
		// 	Member member = new Member();
		// 	member.setUserId(userId);
		//
		// 	RefreshToken refreshToken = new RefreshToken();
		// 	refreshToken.setRefreshToken("mock_refresh_token");
		// 	refreshToken.setUserId(userId);
		//
		//
		// 	JsonObject mockJsonObject = new JsonObject();
		// 	mockJsonObject.addProperty("access_token", "mock_access_token");
		// 	mockJsonObject.addProperty("id", 123456L);
		// 	JsonElement mockJsonElement = mockJsonObject;
		//
		// 	// when
		// 	when(refreshTokenRepository.findByUserIdAndProvider(userId, "KAKAO")).thenReturn(Optional.of(refreshToken));
		// 	when(refreshTokenRepository.findByUserIdAndProvider(userId, "DANIM")).thenReturn(Optional.of(refreshToken));
		//
		//
		// 	kakaoService.kakaoSignout(member);
		//
		// 	// then
		// 	verify(refreshTokenRepository, times(2)).delete(any(RefreshToken.class));
		// }
		}

}
