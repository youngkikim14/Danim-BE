package com.project.danim_be.member.service;

import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.danim_be.member.dto.KakaoMemberInfoDto;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

	private final MemberRepository memberRepository;


	public String kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
		String accessToken = getToken(code);
		System.out.println("accessToken : "+accessToken);
		KakaoMemberInfoDto kakaoUserInfo = getKakaoMemberInfo(accessToken);
		Member kakaoUser = kakaoSignup(kakaoUserInfo);


		return accessToken;
	}

	private String getToken(String code) throws JsonProcessingException {
		// HTTP Header 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HTTP Body 생성
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", "d47514ab2cd7a805e902a2c8d4d70ea6");
		body.add("redirect_uri", "http://localhost:8080/api/user/kakao/callback");
		body.add("code", code);

		// HTTP 요청 보내기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
			new HttpEntity<>(body, headers);
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> response = rt.exchange(
			"https://kauth.kakao.com/oauth/token",
			HttpMethod.POST,
			kakaoTokenRequest,
			String.class
		);

		// HTTP 응답 (JSON) -> 액세스 토큰 파싱
		String responseBody = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		return jsonNode.get("access_token").asText();
	}

	private KakaoMemberInfoDto getKakaoMemberInfo(String accessToken) throws JsonProcessingException {
		// HTTP Header 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HTTP 요청 보내기
		HttpEntity<MultiValueMap<String, String>> MemberInfoRequest = new HttpEntity<>(headers);
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> response = rt.exchange(
			"https://kapi.kakao.com/v2/user/me",
			HttpMethod.POST,
			MemberInfoRequest,
			String.class
		);

		String responseBody = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		Long id = jsonNode.get("id").asLong();
		System.out.println("id = "+id);
		//이메일
		String email = jsonNode.get("kakao_account")
			.get("email").asText();
		System.out.println("email = "+email);
		// 성별
		String gender = jsonNode.get("kakao_account")
			.get("gender").asText();
		System.out.println("gender = "+gender);
		//연령대
		String age_range = jsonNode.get("kakao_account")
			.get("age_range").asText();
		System.out.println("ageRange = "+ age_range);

		log.info("카카오 사용자 정보: " + id +"email"+email);
		return new KakaoMemberInfoDto(email,gender,age_range);
	}

	private Member kakaoSignup(KakaoMemberInfoDto kakaoMemberInfoDto) {
		// DB 에 중복된 Kakao Id 가 있는지 확인
		String email = kakaoMemberInfoDto.getEmail();
		String ageRange = kakaoMemberInfoDto.getAgeRange();
		String gender = kakaoMemberInfoDto.getGender();
		Member kakaoMember = memberRepository.findByUserId(email)
			.orElse(null);
		// System.out.println(Long.toString(kakaoId)+kakaoMember.toString());
		if (kakaoMember == null) {

			// 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
			String kakaoEmail = kakaoMemberInfoDto.getEmail();

			Member sameEmailUser = memberRepository.findByUserId(kakaoEmail).orElse(null);
			if (sameEmailUser != null) {
				kakaoMember = sameEmailUser;
				// 기존 회원정보에 카카오 Id 추가

			} else {

				String password = UUID.randomUUID().toString();



				kakaoMember = new Member(email, password, ageRange,gender);
			}

			memberRepository.save(kakaoMember);
		}
		return kakaoMember;
	}


}
