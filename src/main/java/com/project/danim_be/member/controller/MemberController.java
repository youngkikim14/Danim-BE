package com.project.danim_be.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.member.dto.SignupRequestDto;
import com.project.danim_be.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	public ResponseEntity<Message> signup(@RequestBody SignupRequestDto signupRequestDto, BindingResult bindingResult){

		return memberService.signup(signupRequestDto);
	}


}
