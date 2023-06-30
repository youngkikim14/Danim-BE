package com.project.danim_be.post.service;


import com.project.danim_be.chat.entity.ChatRoom;
import com.project.danim_be.chat.entity.MemberChatRoom;
import com.project.danim_be.chat.repository.ChatRoomRepository;
import com.project.danim_be.chat.repository.MemberChatRoomRepository;
import com.project.danim_be.common.exception.CustomException;
import com.project.danim_be.common.exception.ErrorCode;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.S3Uploader;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.post.dto.RequestDto.ImageRequestDto;
import com.project.danim_be.post.dto.RequestDto.PostRequestDto;
import com.project.danim_be.post.entity.Image;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.repository.ImageRepository;
import com.project.danim_be.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private final PostRepository postRepository;
	private final ImageRepository imageRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final MemberChatRoomRepository memberChatRoomRepository;
	private final S3Uploader s3Uploader;

	//게시글작성
	@Transactional
	public ResponseEntity<Message> createPost(Member member, PostRequestDto requestDto) {

		if(requestDto.getPostTitle() == null) {
			throw new CustomException(ErrorCode.TITLE_IS_NULL);
		} else if(requestDto.getGroupSize() == null) {
			throw new CustomException(ErrorCode.GROUPSIZE_IS_NULL);
		} else if(requestDto.getAgeRange() == null) {
		 	throw new CustomException(ErrorCode.AGERANGE_IS_NULL);
		} else if(requestDto.getGender() == null) {
			throw new CustomException(ErrorCode.GENDER_IS_NULL);
		} else if(requestDto.getLocation() == null) {
			throw new CustomException(ErrorCode.LOCATION_IS_NULL);
		}

		Post post = Post.builder()
			.postTitle(requestDto.getPostTitle())
			.recruitmentStartDate(requestDto.getRecruitmentStartDate())
			.recruitmentEndDate(requestDto.getRecruitmentEndDate())
			.tripStartDate(requestDto.getTripStartDate())
			.tripEndDate(requestDto.getTripEndDate())
			.location(requestDto.getLocation())
			.groupSize(requestDto.getGroupSize())
			.keyword(requestDto.getKeyword())
			.gender(String.join(",", requestDto.getGender()))
			.ageRange(String.join(",", requestDto.getAgeRange()))
			.numberOfParticipants(0)
			.member(member)
			.isDeleted(false)
			.isRecruitmentEnd(false)
			.content(requestDto.getContent())
			.map(requestDto.getMapAPI())
			.build();

		for(String url : requestDto.getContentsImages()) {
			Image image = Image.builder()
				.post(post)
				.imageUrl(url)
				.build();
			imageRepository.save(image);
		}

		String roomName = UUID.randomUUID().toString();
		ChatRoom chatRoom = ChatRoom.builder()
			.roomName(roomName)
			.post(post)
			.adminMemberId(post.getMember().getId())
			.build();
		post.setChatRoom(chatRoom);
		chatRoomRepository.save(chatRoom);

		postRepository.save(post);

		Map<String,Long> postId = new HashMap<>();
		postId.put("postId", post.getId());

		Message message = Message.setSuccess(StatusEnum.OK,"게시글 작성 성공", postId);
		return new ResponseEntity<>(message, HttpStatus.OK);

	}

	//이미지 업로드
	@Transactional
	public ResponseEntity<Message> imageUpload(ImageRequestDto requestDto) {

		MultipartFile imageFile = requestDto.getImage();
		String contentType = imageFile.getContentType();

		if(!Arrays.asList("image/jpeg", "image/png", "image/gif", "image/jpg").contains(contentType)) {
			throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAIL);
		}

		String imageUrl = uploader(imageFile);

		Message message = Message.setSuccess(StatusEnum.OK, "이미지 업로드 성공", imageUrl);
		return new ResponseEntity<>(message, HttpStatus.OK);

	}

	//게시글 수정
	@Transactional
	public ResponseEntity<Message> updatePost(Long id, Member member, PostRequestDto requestDto) {

		List<Image> images = imageRepository.findAllByPostId(id);
		imageRepository.deleteAll(images);
		Post post = postRepository.findById(id)
			.orElseThrow(()	-> new CustomException(ErrorCode.POST_NOT_FOUND));

		if (!post.getMember().getId().equals(member.getId())) {
			throw new CustomException(ErrorCode.NOT_MOD_AUTHORIZED_MEMBER);
		}

		post.update(requestDto);

		//보류
		for(String url : requestDto.getContentsImages()) {
			Image image = Image.builder()
				.post(post)
				.imageUrl(url)
				.build();
			imageRepository.save(image);
		}

		Message message = Message.setSuccess(StatusEnum.OK, "게시글 수정 성공");
		return new ResponseEntity<>(message, HttpStatus.OK);

	}

	//게시글 삭제
	@Transactional
	public ResponseEntity<Message> deletePost(Long id,Member member) {

		List<Image> images = imageRepository.findAllByPostId(id);

		Post post = postRepository.findById(id)
			.orElseThrow(()	-> new CustomException(ErrorCode.POST_NOT_FOUND));

		if(!post.getMember().getId().equals(member.getId())) {
			throw new CustomException(ErrorCode.NOT_DEL_AUTHORIZED_MEMBER);
		}

		imageRepository.deleteAll(images);
		post.delete();

		ChatRoom chatRoom = chatRoomRepository.findByPostId(post.getId());
		List<MemberChatRoom> memberChatRoomList =  memberChatRoomRepository.findAllByChatRoom_Id(chatRoom.getId());
		for(MemberChatRoom memberChatRoom : memberChatRoomList){
			memberChatRoom.InitializationAlarm(0);
		}


		Message message = Message.setSuccess(StatusEnum.OK, "게시글 삭제 성공");
		return new ResponseEntity<>(message, HttpStatus.OK);

	}

	public String uploader(MultipartFile imageFile){

		String file;
		try {
			file = s3Uploader.upload(imageFile);
		} catch (IOException e) {
			throw new CustomException(ErrorCode.FILE_CONVERT_FAIL);
		}
		return file;

	}

	@Transactional
	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")    // 매일 00:00:00 실행
	public void endRecruitmentDate() {

		List<Post> postList = postRepository.findAll();
		LocalDate today = LocalDate.now().minusDays(1);
		for (Post post : postList) {
			if (post.getIsRecruitmentEnd().equals(false)) {
				if (today.isAfter(post.getRecruitmentEndDate())) {
					post.endRecruitmentDate();
					postRepository.save(post);
				}
			}
		}

	}
}
