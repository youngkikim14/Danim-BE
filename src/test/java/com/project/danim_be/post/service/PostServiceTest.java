package com.project.danim_be.post.service;

import static com.project.danim_be.post.entity.Gender.*;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;

import com.project.danim_be.chat.repository.ChatRoomRepository;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.S3Uploader;
import com.project.danim_be.member.dto.RequestDto.SignupRequestDto;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.service.MemberService;
import com.project.danim_be.post.dto.RequestDto.PostRequestDto;
import com.project.danim_be.post.entity.Gender;
import com.project.danim_be.post.entity.Location;
import com.project.danim_be.post.repository.ImageRepository;
import com.project.danim_be.post.repository.PostRepository;

@SpringBootTest
@Nested
@DisplayName("createPost ")
class PostServiceTest {

	@Autowired PostService postService;
	@Autowired MemberService memberService;
	@Autowired PostRepository postRepository;
	@Autowired ImageRepository imageRepository;
	@Autowired ChatRoomRepository chatRoomRepository;
	@Autowired S3Uploader s3Uploader;
	@Mock
	private SignupRequestDto signupRequestDto;

	@BeforeEach
	void signup() {
		signupRequestDto = new SignupRequestDto("test41@co.kr","a45678a!a","테스트닉네임" ,"20대", FEMALE,true,true);
		memberService.signup(signupRequestDto);
	}

	@Test
	@DisplayName("게시글 작성 성공")
	void createPost() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Member member = new Member();
		memberService.signup(signupRequestDto);

		PostRequestDto postRequestDto = new PostRequestDto();
		postRequestDto.setPostTitle("여름바다고고");											//제목
		postRequestDto.setRecruitmentEndDate(dateFormat.parse("2023-07-07"));		//모집시작날짜
		postRequestDto.setRecruitmentStartDate(dateFormat.parse("2023-07-30"));		//모집마감날짜
		postRequestDto.setTripStartDate(dateFormat.parse("2023-08-01"));				//여행시작날짜
		postRequestDto.setTripEndDate(dateFormat.parse("2023-08-15"));
		List<String> ageRange = Arrays.asList("20대", "30대");
		postRequestDto.setAgeRange(ageRange);												//여행마감날짜
		postRequestDto.setGender(FEMALE);													//성별
		postRequestDto.setGroupSize(5);														//모집인원
		postRequestDto.setLocation(Location.SEOUL);											//지역
		postRequestDto.setContent("여기는 그냥 스트링값으로 들어갑니다.");							//내용
		postRequestDto.setKeyword("여기도 그냥 스트링값이 들어갑니다.");							//키워드
		List<String> imageUrl = Arrays.asList("이미지 url1", "이미지 url2");
		postRequestDto.setImageUrls(imageUrl);												//이미지 URL
		postRequestDto.setMapAPI("여기도 스트링");												//지도좌표

		ResponseEntity<Message> response = postService.createPost(member, postRequestDto);






	}
}