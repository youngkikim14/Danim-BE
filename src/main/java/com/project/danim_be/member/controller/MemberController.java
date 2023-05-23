package com.project.danim_be.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.member.dto.SignupRequestDto;
import com.project.danim_be.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	public ResponseEntity<Message> signup(@Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult bindingResult){
		if(bindingResult.hasErrors()){
			String errMessage =  bindingResult.getAllErrors().get(0).getDefaultMessage();
			Message apiResult = Message.builder()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.message(errMessage)
				.build();
			return ResponseEntity.badRequest().body(apiResult);
		}else{
			return memberService.signup(signupRequestDto);
		}


	}


}
