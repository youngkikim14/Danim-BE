package com.project.danim_be.post.service;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.S3Uploader;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.post.dto.ContentRequestDto;
import com.project.danim_be.post.dto.PostRequestDto;
import com.project.danim_be.post.dto.PostResponseDto;
import com.project.danim_be.post.entity.Content;
import com.project.danim_be.post.entity.ContentType;
import com.project.danim_be.post.entity.Image;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.repository.ContentRepository;
import com.project.danim_be.post.repository.ImageRepository;
import com.project.danim_be.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

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
		logger.info("Received PostRequestDto: {}", requestDto.toString());
		logger.info("Received PostRequestDto: {}", requestDto.getContents().toString());

		Post post = Post.builder()
			.postTitle(requestDto.getPostTitle())
			.recruitmentStartDate(requestDto.getRecruitmentStartDate())
			.recruitmentEndDate(requestDto.getRecruitmentEndDate())
			.tripStartDate(requestDto.getTripStartDate())
			.tripEndDate(requestDto.getTripEndDate())
			.location(requestDto.getLocation())
			.groupSize(requestDto.getGroupSize())
			.ageRange(requestDto.getAgeRange())
			.keyword(requestDto.getKeyword())
			.member(member)
			.contents(new ArrayList<>())
			.build();
		postRepository.save(post);
		if (requestDto.getContents() != null) {
			for (ContentRequestDto contentDto : requestDto.getContents()) {
				if(contentDto.getType().toUpperCase().equals("HEADING")){
					Content content = Content.builder()
						.type(contentDto.getType())
						.level(contentDto.getLevel())
						.text(contentDto.getText())
						.post(post)
						.build();
					contentRepository.save(content);
					post.getContents().add(content);
				}else if(contentDto.getType().toUpperCase().equals("PARAGRAPH")){
					Content content = Content.builder()
						.type(contentDto.getType())
						.text(contentDto.getText())
						.post(post)
						.build();
					contentRepository.save(content);
					post.getContents().add(content);
				}else if(contentDto.getType().toUpperCase().equals("IMAGE")){
					Content content = Content.builder()
						.type(contentDto.getType())
						.post(post)
						.build();
					MultipartFile imageFile = contentDto.getSrc();
					String imageUrl = uploader(imageFile);
					Image image = Image.builder()
						.imageUrl(imageUrl)
						.imageName(contentDto.getSrc().getOriginalFilename())
						.content(content)
						.build();
					imageRepository.save(image);
					contentRepository.save(content);
					post.getContents().add(content);

				}

			}
		}
		// PostResponseDto postResponseDto = new PostResponseDto(post);

		Message message = Message.setSuccess(StatusEnum.OK,"게시글 작성 성공");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	//게시글 조회

	public String uploader(MultipartFile imageFile){
		String file = null;
		try {
			file = s3Uploader.upload(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;

	}




}
