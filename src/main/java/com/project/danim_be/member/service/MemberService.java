package com.project.danim_be.member.service;

import org.springframework.http.ResponseEntity;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.member.dto.SignupRequestDto;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
	public ResponseEntity<Message> signup(SignupRequestDto signupRequestDto) {
		return null;
	}
}
