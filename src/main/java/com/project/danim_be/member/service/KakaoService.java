package com.project.danim_be.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.RandomNickname;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.member.dto.KakaoMemberInfoDto;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;
import com.project.danim_be.security.jwt.JwtUtil;
import com.project.danim_be.security.jwt.TokenDto;
import com.project.danim_be.security.refreshToken.RefreshToken;
import com.project.danim_be.security.refreshToken.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

	private final JwtUtil jwtUtil;
	private final RefreshTokenRepository refreshTokenRepository;

	private final MemberRepository memberRepository;
	@Value("${kakao.client.id}")
	private String clientId;
	@Value("${kakao.redirect.uri}")
	private String redirectUri;
	@Value("${kakao.token.url}")
	private String tokenUrl;
	@Value("${kakao.info.url}")
	private String userInfoUrl;


	public ResponseEntity<Message> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
		String accessToken = getToken(code);
		System.out.println("accessToken : "+accessToken);
		KakaoMemberInfoDto kakaoUserInfo = getKakaoMemberInfo(accessToken);
		Member kakaoUser = kakaoSignup(kakaoUserInfo);
		System.out.println(kakaoUser.getUserId());

		TokenDto tokenDto = jwtUtil.createAllToken(kakaoUser.getUserId());
		setHeader(response, tokenDto);
		Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(kakaoUser.getUserId());
		if (refreshToken.isPresent()) {
			refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
		} else {
			RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), kakaoUser.getUserId(), "DANIM");
			refreshTokenRepository.save(newToken);
		}

		Message message = Message.setSuccess(StatusEnum.OK,"회원 가입 성공");
		return new ResponseEntity<>(message, HttpStatus.OK);
		// return accessToken;
	}

	private String getToken(String code) throws JsonProcessingException {
		// HTTP Header 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HTTP Body 생성
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", clientId);
		body.add("redirect_uri", redirectUri);
		body.add("code", code);

		// HTTP 요청 보내기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
			new HttpEntity<>(body, headers);
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> response = rt.exchange(
			tokenUrl,
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
			userInfoUrl,
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
		String nickname = RandomNickname.getRandomNickname();
		Member kakaoMember = memberRepository.findByUserId(email)
			.orElse(null);
		if (kakaoMember == null) {

			String password = UUID.randomUUID().toString();
			// kakaoMember = new Member(email, password, ageRange,gender,nickname);
			Member member = Member.builder()
				.email(kakaoMemberInfoDto.getEmail())
				.ageRange(kakaoMemberInfoDto.getAgeRange())
				.gender(kakaoMemberInfoDto.getGender())
				.nickname(nickname)
				.password(password)
				.provider("KAKAO")
				.build();

			System.out.println(nickname);

			memberRepository.save(member);
			return member;
		}
		return kakaoMember;
	}

	private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
		response.addHeader(JwtUtil.ACCESS_KEY, tokenDto.getAccessToken());
		response.addHeader(JwtUtil.REFRESH_KEY, tokenDto.getRefreshToken());
	}
}
