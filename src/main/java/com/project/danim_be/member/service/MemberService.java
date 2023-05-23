package com.project.danim_be.member.service;

import java.util.Optional;

import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.danim_be.common.exception.CustomException;
import com.project.danim_be.common.exception.ErrorCode;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.member.dto.SignupRequestDto;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {


	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;


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
		Member member =  new Member(userId,password,nickname,ageRange);
		memberRepository.save(member);

		Message message = Message.setSuccess(StatusEnum.OK,"회원 가입 성공");
		return new ResponseEntity<>(message, HttpStatus.OK);


	}
}
