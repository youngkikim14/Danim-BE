package com.project.danim_be.post.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;
import com.project.danim_be.post.dto.PostRequestDto;
import com.project.danim_be.post.repository.PostRepository;
import com.project.danim_be.security.auth.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	//게시글작성
	@Transactional
	public void createPost(Member member, PostRequestDto requestDto) {


	}
}
