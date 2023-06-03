package com.project.danim_be.chat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.danim_be.chat.dto.ChatRoomResponseDto;
import com.project.danim_be.chat.entity.ChatRoom;
import com.project.danim_be.chat.entity.QMemberChatRoom;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.repository.PostRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final PostRepository postRepository;
	private final JPAQueryFactory queryFactory;

	public ResponseEntity<Message> myChatRoom(Long id) {
		List<Post> postList = postRepository.findByMember_Id(id);
		List<ChatRoomResponseDto> chatRoomResponseDtoList = new ArrayList<>();
		for (Post post : postList) {
			ChatRoom chatRoom = post.getChatRoom();
			ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto(chatRoom);
			chatRoomResponseDtoList.add(chatRoomResponseDto);
		}
		return ResponseEntity.ok(Message.setSuccess(StatusEnum.OK,"내가 만든 채팅방",chatRoomResponseDtoList));
	}

	//내가 신청한 채팅방 목록조회
	public ResponseEntity<Message> myJoinChatroom(Long id) {
		QMemberChatRoom qMemberChatRoom = QMemberChatRoom.memberChatRoom;
		List<ChatRoom> chatRoomList = queryFactory
			.select(qMemberChatRoom.chatRoom)
			.from(qMemberChatRoom)
			.where(qMemberChatRoom.member.id.eq(id))
			.fetch();
		List<ChatRoomResponseDto> chatRoomResponseDtoList = new ArrayList<>();
		for (ChatRoom chatroom : chatRoomList) {
			if (!chatroom.getPost().getMember().getId().equals(id)){
				chatRoomResponseDtoList.add(new ChatRoomResponseDto(chatroom));
			}
		}
		return ResponseEntity.ok(Message.setSuccess(StatusEnum.OK,"내가 참여한 채팅방", chatRoomResponseDtoList)); // 쿼리문 짜기
	}

	//채팅방 참여(웹소켓연결/방입장) == 매칭 신청 버튼
	public ResponseEntity<Message> joinChatRoom(Long id, Member member) {
		return null;
	}
}
