package com.project.danim_be.chat.service;


import com.project.danim_be.chat.dto.ChatDto;
import com.project.danim_be.common.exception.CustomException;
import com.project.danim_be.common.exception.ErrorCode;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.chat.entity.ChatMessage;
import com.project.danim_be.chat.entity.ChatRoom;
import com.project.danim_be.chat.entity.MemberChatRoom;
import com.project.danim_be.chat.repository.ChatMessageRepository;
import com.project.danim_be.chat.repository.ChatRoomRepository;
import com.project.danim_be.chat.repository.MemberChatRoomRepository;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final MemberChatRoomRepository memberChatRoomRepository;
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;

	@Transactional
	public void visitMember(ChatDto chatDto){
		String roomId = chatDto.getRoomId();
		String sender = chatDto.getSender();

		Member member = memberRepository.findByNickname(sender)
			.orElseThrow(() -> new IllegalArgumentException("낫파운드유저"));

		ChatRoom chatRoom= chatRoomRepository.findByRoomId(roomId)
			.orElseThrow(() -> new IllegalArgumentException("낫파운드룸"));
		MemberChatRoom memberChatRoom = new MemberChatRoom(member, chatRoom);

		memberChatRoomRepository.save(memberChatRoom);

	}

	//메시지저장
	@Transactional
	public void sendMessage(ChatDto chatDto) {
		String roomId = chatDto.getRoomId();
		ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
			.orElseThrow(() -> new IllegalArgumentException("채팅방없음 커스텀필요 NOT_FOUND_ROOM"));;

		ChatMessage chatMessage= new ChatMessage(chatDto,chatRoom);
		chatMessageRepository.save(chatMessage);

	}

	//메시지조회
	@Transactional(readOnly = true)
	public ResponseEntity<Message> chatList(ChatDto chatDto){
		String roomId = chatDto.getRoomId();
		String nickName= chatDto.getSender();

		Member member = memberRepository.findByNickname(nickName)
			.orElseThrow(() -> new IllegalArgumentException("MEMBERNOTFOUND"));;;

		if (chatDto.getSender().equals(member.getNickname()) && isFirstVisit(member.getId(),roomId)){
			List<ChatDto> allChats = allChatList(chatDto);
			Message message = Message.setSuccess(StatusEnum.OK,"게시글 작성 성공");
			return new ResponseEntity<>(message, HttpStatus.OK);
		}


		Message message = Message.setSuccess(StatusEnum.OK,"게시글 작성 성공");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	private boolean isFirstVisit(Long memberId, String roomId){
		return !memberChatRoomRepository.existsByMember_IdAndChatRoom_RoomId(memberId, roomId);
		//xistsBy 메소드는 특정 조건을 만족하는 데이터가 존재하는지를 검사하고
		// 그 결과를 boolean으로 반환
	}

	private List<ChatDto> allChatList(ChatDto chatDto){
		String roomId = chatDto.getRoomId();
		ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
			.orElseThrow(() -> new IllegalArgumentException("채팅방없음 커스텀필요 NOT_FOUND_ROOM"));

		List<ChatMessage> chatList = chatMessageRepository.findAllByChatRoom(chatRoom);

		// Convert ChatMessage list to ChatDto list
		List<ChatDto> chatDtoList = chatList.stream()
			.map(chatMessage -> ChatDto.builder()
				.type(chatMessage.getType())
				.roomId(chatMessage.getChatRoom().getRoomId())
				.sender(chatMessage.getSender())
				.message(chatMessage.getMessage())
				.build())
			.collect(Collectors.toList());

		return chatDtoList;

	}

	public void leaveChatRoom(String sender, String roomId) {

	}
}
