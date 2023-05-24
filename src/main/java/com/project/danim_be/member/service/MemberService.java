package com.project.danim_be.member.service;

import static com.project.danim_be.common.exception.ErrorCode.*;

import java.util.Optional;

import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.danim_be.common.exception.CustomException;
import com.project.danim_be.common.exception.ErrorCode;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.member.dto.LoginRequestDto;
import com.project.danim_be.member.dto.LoginResponseDto;
import com.project.danim_be.member.dto.SignupRequestDto;

import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;
import com.project.danim_be.security.jwt.JwtUtil;
import com.project.danim_be.security.jwt.TokenDto;
import com.project.danim_be.security.refreshToken.RefreshToken;
import com.project.danim_be.security.refreshToken.RefreshTokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtUtil jwtUtil;

	//회원가입
	@Transactional
	public ResponseEntity<Message> signup(SignupRequestDto signupRequestDto) {

		String userId 	= signupRequestDto.getUserId();
		String password = passwordEncoder.encode(signupRequestDto.getPassword());
		String nickname	= signupRequestDto.getNickname();
		String ageRange = signupRequestDto.getAgeRange();

		Optional<Member> findMember = memberRepository.findByUserId(userId);
		if(findMember.isPresent()){
			throw new CustomException(ErrorCode.DUPLICATE_IDENTIFIER);
		}

		Optional<Member> foundMemberNickname = memberRepository.findByNickname(nickname);
		if (foundMemberNickname.isPresent()) {
			throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
		}

		Member member = Member.builder()
			.email(userId)
			.ageRange(ageRange)
			.gender("")
			.nickname(nickname)
			.password(password)
			.provider("GENERAL")
			.build();

		System.out.println(nickname);
		memberRepository.save(member);

		Message message = Message.setSuccess(StatusEnum.OK,"회원 가입 성공");
		return new ResponseEntity<>(message, HttpStatus.OK);

	}
	//로그인
	@Transactional
	public ResponseEntity<Message> login(LoginRequestDto requestDto, HttpServletResponse response) {

		String userId = requestDto.getUserId();
		String password = requestDto.getPassword();

		Member member = memberRepository.findByUserId(userId).orElseThrow(
			() -> new CustomException(ErrorCode.ID_NOT_FOUND)
		);

		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}
		TokenDto tokenDto = jwtUtil.createAllToken(userId);

		Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(member.getUserId());
		if (refreshToken.isPresent()) {
			refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
		} else {
			RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), member.getUserId());
			refreshTokenRepository.save(newToken);
		}
		setHeader(response, tokenDto);

		LoginResponseDto loginResponseDto =new LoginResponseDto(member);
		Message message = Message.setSuccess(StatusEnum.OK, "로그인 성공", loginResponseDto);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	//로그아웃
	@Transactional
	public ResponseEntity<Message> logout(Member member, HttpServletRequest request) {
		Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(member.getUserId());

		String accessToken = request.getHeader("ACCESS_KEY").substring(7);
		if(refreshToken.isPresent()){
			Long tokenTime = jwtUtil.getExpirationTime(accessToken);
			refreshTokenRepository.deleteByUserId(member.getUserId());
			Message message = Message.setSuccess(StatusEnum.OK,"로그아웃 성공", member.getUserId());
			return new ResponseEntity<>(message, HttpStatus.OK);
		}
		throw new CustomException(USER_NOT_FOUND);
	}
	// 헤더 셋팅
	private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
		response.addHeader(JwtUtil.ACCESS_KEY, tokenDto.getAccessToken());
	   	response.addHeader(JwtUtil.REFRESH_KEY, tokenDto.getRefreshToken());
	}
}
