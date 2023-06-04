package com.project.danim_be.post.service;


import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.danim_be.common.exception.CustomException;
import com.project.danim_be.common.exception.ErrorCode;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.S3Uploader;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.post.dto.ContentRequestDto;
import com.project.danim_be.post.dto.PostRequestDto;
import com.project.danim_be.post.dto.PostResponseDto;
import com.project.danim_be.post.entity.Content;
import com.project.danim_be.post.entity.Image;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.repository.ContentRepository;
import com.project.danim_be.post.repository.ImageRepository;
import com.project.danim_be.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final ContentRepository contentRepository;
	private final ImageRepository imageRepository;
	private final S3Uploader s3Uploader;

	private static final Logger logger = LoggerFactory.getLogger(PostService.class);

	//게시글작성
	@Transactional
	public ResponseEntity<Message> createPost(Member member, PostRequestDto requestDto) {
		// logger.info("Received PostRequestDto: {}", requestDto.toString());
		// logger.info("Received PostRequestDto: {}", requestDto.getContents().toString());
		System.out.println("map====================" + requestDto.getMapAPI());
		

		Post post = Post.builder()
			.postTitle(requestDto.getPostTitle())
			.recruitmentStartDate(requestDto.getRecruitmentStartDate())
			.recruitmentEndDate(requestDto.getRecruitmentEndDate())
			.tripStartDate(requestDto.getTripStartDate())
			.tripEndDate(requestDto.getTripEndDate())
			.location(requestDto.getLocation())
			.groupSize(requestDto.getGroupSize())
			.ageRange(String.join(",", requestDto.getAgeRange()))		//이부분은 공부해볼게요
			.gender(String.join(",", requestDto.getGender()))
			.keyword(requestDto.getKeyword())
			.numberOfParticipants(0)
			.member(member)
			.mapAPI(requestDto.getMapAPI())
			.contents(new ArrayList<>())
			.build();
		postRepository.save(post);
		saveContents(requestDto, post);

		// PostResponseDto postResponseDto = new PostResponseDto(post);
		Message message = Message.setSuccess(StatusEnum.OK,"게시글 작성 성공");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	//게시글 수정
	@Transactional
	public ResponseEntity<Message> updatePost(Long id,Member member, PostRequestDto requestDto) {

		Post post = postRepository.findById(id).orElseThrow(()
			->new CustomException(ErrorCode.POST_NOT_FOUND));

		if (!post.getMember().getId().equals(member.getId())) {
			throw new CustomException(ErrorCode.NOT_MOD_AUTHORIZED_MEMBER);
		}

		post.update(requestDto);

		contentRepository.deleteByPostId(id);

		// List<Content> contents =  post.getContents();

		// for (Content content : contents){
		// 	Image image =  content.getImage();
		// 	if(image!=null){
		// 		String imageUrl =  image.getImageUrl();
		// 		s3Uploader.delete(imageUrl);
		// 	}
		// }


		saveContents(requestDto, post);

		Message message = Message.setSuccess(StatusEnum.OK, "게시글 수정 성공");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	//게시글 삭제
	@Transactional
	public ResponseEntity<Message> deletePost(Long id,Member member) {
		
		Post post = postRepository.findById(id).orElseThrow(()
			->new CustomException(ErrorCode.POST_NOT_FOUND));

		if (!post.getMember().getId().equals(member.getId())) {
			throw new CustomException(ErrorCode.NOT_DEL_AUTHORIZED_MEMBER);
		}
		
		post.delete();

		Message message = Message.setSuccess(StatusEnum.OK, "게시글 삭제 성공");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	public String uploader(MultipartFile imageFile){
		String file = null;
		try {
			file = s3Uploader.upload(imageFile);
		} catch (IOException e) {
			throw new CustomException(ErrorCode.FILE_CONVERT_FAIL);
		}
		return file;
	}

	private void saveContents(PostRequestDto requestDto, Post post) {
		if (requestDto.getContents() != null) {
			for (ContentRequestDto contentDto : requestDto.getContents()) {
				Content content = switch (contentDto.getType()) {
					case "heading" -> Heading(contentDto, post);
					case "paragraph" -> Paragraph(contentDto, post);
					case "image" -> Image(contentDto, post);
					case "enter" -> Enter(contentDto, post);
					default -> null;
				};
				if (content != null) {
					post.getContents().add(content);
				}
			}
		}
	}

	private Content Enter(ContentRequestDto contentDto, Post post) {
		Content content = Content.builder()
			.type(contentDto.getType())
			.text(contentDto.getText())
			.post(post)
			.build();
		contentRepository.save(content);
		return content;
	}

	private Content Heading(ContentRequestDto contentDto, Post post) {
		Content content = Content.builder()
			.type(contentDto.getType())
			.level(contentDto.getLevel())
			.text(contentDto.getText())
			.post(post)
			.build();
		contentRepository.save(content);
		return content;
	}

	private Content Paragraph(ContentRequestDto contentDto, Post post) {
		Content content = Content.builder()
			.type(contentDto.getType())
			.text(contentDto.getText())
			.post(post)
			.build();
		contentRepository.save(content);
		return content;
	}

	private Content Image(ContentRequestDto contentDto, Post post) {
		MultipartFile imageFile = contentDto.getSrc();
		String imageUrl = uploader(imageFile);
		Content content = Content.builder()
			.type(contentDto.getType())
			.post(post)
			.build();
		contentRepository.save(content);
		Image image = Image.builder()
			.imageUrl(imageUrl)
			.imageName(contentDto.getSrc().getOriginalFilename())
			.content(content)
			.build();
		imageRepository.saveAndFlush(image);
		return content;
	}

	public ResponseEntity<Message> readPost(Long id) {

		Post post = postRepository.findById(id).orElseThrow(()
			->new CustomException(ErrorCode.POST_NOT_FOUND));

		PostResponseDto postResponseDto = new PostResponseDto(post);

		Message message = Message.setSuccess(StatusEnum.OK, "게시글 단일 조회 성공",postResponseDto);
		return new ResponseEntity<>(message, HttpStatus.OK);

	}
}
