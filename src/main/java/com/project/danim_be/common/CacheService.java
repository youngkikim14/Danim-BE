package com.project.danim_be.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.danim_be.chat.repository.ChatRoomRepository;
import com.project.danim_be.common.exception.CustomException;
import com.project.danim_be.common.exception.ErrorCode;
import com.project.danim_be.member.repository.MemberRepository;
import com.project.danim_be.post.dto.ResponseDto.PostResponseDto;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private RedisTemplate<String,PostResponseDto> postResponseDtoRedisTemplate;
	private final MemberRepository memberRepository;
	private final ChatRoomRepository chatRoomRepository;

	// @Cacheable(key = "#id", value="Post")
	// public Post findPostById(Long id) {
	// 	System.out.println("====post====");
	// 	return postRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
	// }

	@Cacheable(key = "#id", value="PostResponseDto")
	public PostResponseDto postRes(Long id) throws JsonProcessingException {

		System.out.println("====post====");
		// 레디스에서 PostResponseDto 형태로 데이터를 가져옵니다.
		PostResponseDto value = postResponseDtoRedisTemplate.opsForValue().get(String.valueOf(id));

		if(value == null) {
			System.out.println("==== No data ====");
			// 레디스에 데이터가 없는 경우 DB에서 데이터를 가져옵니다.
			Post post = postRepository.findById(id)
				.orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
			PostResponseDto postResponseDto = new PostResponseDto(post);
			// 레디스에 데이터를 저장합니다.
			postResponseDtoRedisTemplate.opsForValue().set(String.valueOf(id), postResponseDto);
			return postResponseDto;
		}

		System.out.println("Value type from Redis: " + value.getClass().getName());
		return value;
	}

	// @Cacheable(key = "#roomName", value="findByRoomName")
	// public ChatRoom findByRoomName(String roomName){
	// 	return  chatRoomRepository.findByRoomName(roomName)
	// 		.orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));
	// 	// System.out.println("====ChatRoom====");
	// }
	// @Cacheable(key = "#roomName", value="findByNickname")
	// public Member findByNickname(ChatDto chatDto){
	// 	System.out.println("====Member====");
	// 	return memberRepository.findByNickname(chatDto.getSender())
	// 		.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	// }

}
