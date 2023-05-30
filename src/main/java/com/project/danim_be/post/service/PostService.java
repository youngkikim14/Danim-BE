package com.project.danim_be.post.service;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.post.dto.ContentRequestDto;
import com.project.danim_be.post.dto.ImageRequestDto;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final ContentRepository contentRepository;
	private final ImageRepository imageRepository;


	// 이곳에 Logger 인스턴스를 선언합니다.
	private static final Logger logger = LoggerFactory.getLogger(PostService.class);

	//게시글작성
	@Transactional
	public ResponseEntity<Message> createPost(Member member, PostRequestDto requestDto) {
		// logger.info("Received PostRequestDto: {}", requestDto.toString());
		Post post = Post.builder()
			.postTitle(requestDto.getPostTitle())
			.startDate(requestDto.getStartDate())
			.endDate(requestDto.getEndDate())
			.location(requestDto.getLocation())
			.groupSize(requestDto.getGroupSize())
			.ageRange(requestDto.getAgeRange())
			.keyword(requestDto.getKeyword())
			.member(member)
			.contents(new ArrayList<>())
			.build();
		if (requestDto.getGroupSize() == 1){
			post.setTypeOfMeeting(true);
		} else {post.setTypeOfMeeting(false);}
		postRepository.save(post);
		if (requestDto.getContents() != null) {
			for (ContentRequestDto contentDto : requestDto.getContents()) {
				Content content = Content.builder()
					.type(contentDto.getType())
					.level(contentDto.getLevel())
					.text(contentDto.getText())
					.post(post)
					.imageLists(new ArrayList<>())
					.build();
				contentRepository.save(content);
				post.getContents().add(content);
				if (contentDto.getImageLists() != null) {
					for (ImageRequestDto imageDto : contentDto.getImageLists()) {
						Image image = Image.builder()
							.imageName(imageDto.getImageName())
							.imageUrl(imageDto.getImageUrl())
							.content(content)
							.build();
						imageRepository.save(image);
						content.getImageLists().add(image);
					}
				}

			}
		}
		PostResponseDto postResponseDto = new PostResponseDto(post);

		Message message = Message.setSuccess(StatusEnum.OK,"게시글 작성 성공",postResponseDto);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}


}
