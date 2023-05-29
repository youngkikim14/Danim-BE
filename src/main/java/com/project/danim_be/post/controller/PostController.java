package com.project.danim_be.post.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.post.dto.PostRequestDto;
import com.project.danim_be.post.service.PostService;
import com.project.danim_be.security.auth.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

public class PostController {

	private final PostService postService;

	@PostMapping(value = "api/post",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Message> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PostRequestDto requestDto){


		postService.createPost(userDetails.getMember(),requestDto);
		return null;

	}



}
