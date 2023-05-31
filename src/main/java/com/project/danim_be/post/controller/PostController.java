package com.project.danim_be.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.post.dto.PostRequestDto;
import com.project.danim_be.post.service.PostService;
import com.project.danim_be.security.auth.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Tag(name = "PostController", description = "게시글 API")
@RestController
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@Operation(summary = "게시글 작성 API", description = "게시글 작성")
	@PostMapping(value = "api/post",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity<Message> createPost(@AuthenticationPrincipal final UserDetailsImpl userDetails, @ModelAttribute final PostRequestDto requestDto){

		return	postService.createPost(userDetails.getMember(),requestDto);

	}



}
