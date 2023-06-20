package com.project.danim_be.chat.service;

import com.project.danim_be.chat.dto.ChatListResponseDto;
import com.project.danim_be.chat.dto.ChatRoomResponseDto;
import com.project.danim_be.chat.entity.ChatMessage;
import com.project.danim_be.chat.entity.ChatRoom;
import com.project.danim_be.chat.entity.MemberChatRoom;
import com.project.danim_be.chat.entity.QMemberChatRoom;
import com.project.danim_be.chat.repository.ChatMessageRepository;
import com.project.danim_be.chat.repository.ChatRoomRepository;
import com.project.danim_be.chat.repository.MemberChatRoomRepository;
import com.project.danim_be.common.exception.CustomException;
import com.project.danim_be.common.exception.ErrorCode;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.repository.PostRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final MemberChatRoomRepository memberChatRoomRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final JPAQueryFactory queryFactory;
	private final ChatMessageRepository chatMessageRepository;

	//내가 쓴글의 채팅방 목록조회
	public ResponseEntity<Message> myChatRoom(Long id) {
		List<Post> postList = postRepository.findByMember_Id(id);
		List<ChatListResponseDto> chatListResponseDtoList = new ArrayList<>();
		for (Post post : postList) {
			ChatRoom chatRoom = post.getChatRoom();
			ChatListResponseDto chatListResponseDto = new ChatListResponseDto(chatRoom);
			chatListResponseDtoList.add(chatListResponseDto);
		}
		return ResponseEntity.ok(Message.setSuccess(StatusEnum.OK, "내가 만든 채팅방", chatListResponseDtoList));
	}

	// 내가 신청한 채팅방 목록조회
	public ResponseEntity<Message> myJoinChatroom(Long id) {
		QMemberChatRoom qMemberChatRoom = QMemberChatRoom.memberChatRoom;
		List<ChatRoom> chatRoomList = queryFactory
				.select(qMemberChatRoom.chatRoom)
				.from(qMemberChatRoom)
				.where(qMemberChatRoom.member.id.eq(id))
				.fetch();
		List<ChatListResponseDto> chatListResponseDtoList = new ArrayList<>();
		for (ChatRoom chatroom : chatRoomList) {
			if (!chatroom.getPost().getMember().getId().equals(id)) {
				chatListResponseDtoList.add(new ChatListResponseDto(chatroom));
			}
		}
		return ResponseEntity.ok(Message.setSuccess(StatusEnum.OK, "내가 참여한 채팅방", chatListResponseDtoList)); // 쿼리문 짜기
	}

	//채팅방 참여(웹소켓연결/방입장) == 매칭 신청 버튼
	@Transactional
	public ResponseEntity<Message> joinChatRoom(Long id, Member member) {

		ChatRoom chatRoom = chatRoomRepository.findById(id)
				.orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));
		//방을찾고
		Post post = postRepository.findByChatRoom_Id(id)
				.orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
		//삭제된 게시글
		if(post.getIsDeleted().equals(true)) {
			throw new CustomException(ErrorCode.POST_NOT_FOUND);
		}
		//방장(작성자) 체크
		// if(member.getId().equals(post.getMember().getId())) {
		// 	throw new CustomException(ErrorCode.ADMIN_USER);
		// }
		//연령대 조건 비교하고
		if (!post.getAgeRange().contains(member.getAgeRange())) {
			throw new CustomException(ErrorCode.NOT_CONTAIN_AGERANGE);
		}
		//성별 조건 비교하고
		if (!post.getGender().contains(member.getGender())) {
			throw new CustomException(ErrorCode.NOT_CONTAIN_GENDER);
		}

		Date recruitmentEndDate = post.getRecruitmentEndDate();
		// LocalDate 타입으로 변환
		LocalDate localDate = new java.sql.Date(recruitmentEndDate.getTime()).toLocalDate();
		LocalDate today = LocalDate.now();

		// 현재 날짜가 모집 종료일보다 늦다면 true
		boolean afterDate = today.isAfter(localDate);
		// 모집이 종료되면
		if (afterDate) throw new CustomException(ErrorCode.EXPIRED_RECRUIT);
		MemberChatRoom memberChatRooms = memberChatRoomRepository.findByMemberAndChatRoom(member, chatRoom).orElse(null);
		if (memberChatRooms != null) {
			if (memberChatRooms.getKickMember()) {
				throw new CustomException(ErrorCode.USER_KICKED);
			}
		}
		if (post.getNumberOfParticipants() < post.getGroupSize() || memberChatRooms!=null ) {
			List<MemberChatRoom> memberChatRoomList = memberChatRoomRepository.findAllByChatRoom_Id(id);
			List<Map<String, Object>> userInfoList = new ArrayList<>();
			List<Object> chatRecord =new ArrayList<>();
			for (MemberChatRoom memberChatRoom : memberChatRoomList) {
				Map<String, Object> userInfo = new HashMap<>();
				// Map<String, Object> chatRecords =new HashMap<>();
				userInfo.put("nickname", memberChatRoom.getMember().getNickname());
				userInfo.put("imageUrl", memberChatRoom.getMember().getImageUrl());
				userInfoList.add(userInfo);
			}

			if(memberChatRooms != null) {
				Date from = Date.from(memberChatRooms.getFirstJoinRoom().atZone(ZoneId.systemDefault()).toInstant());
				List<ChatMessage> chatMessages =chatMessageRepository.findByChatRoomId(id);
				List<ChatMessage> filteredChatMessages = new ArrayList<>();
				for (ChatMessage message : chatMessages) {
					if (message.getCreatedAt().after(from) ) {
						filteredChatMessages.add(message);
					}
				}
				chatRecord.add(filteredChatMessages);
			}

			ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto(chatRoom.getRoomName(),userInfoList,chatRecord);

			return ResponseEntity.ok(Message.setSuccess(StatusEnum.OK, "모임 신청 완료", chatRoomResponseDto));
		}else {
			throw new CustomException(ErrorCode.COMPLETE_MATCHING);
		}

}

	//신청취소(나가기)
	@Transactional
	public ResponseEntity<Message> leaveChatRoom(Long id, Member member) {
		QMemberChatRoom qMemberChatRoom = QMemberChatRoom.memberChatRoom;

		ChatRoom chatRoom = chatRoomRepository.findById(id)
				.orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));
		Post post = postRepository.findByChatRoom_Id(id)
				.orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
		MemberChatRoom leaveMember = memberChatRoomRepository.findByMemberAndChatRoom(member, chatRoom)
				.orElseThrow(() -> new CustomException(ErrorCode.FAIL_FIND_MEMBER_CHAT_ROOM));

		if(member.getId().equals(leaveMember.getMember().getId())) {
			post.decNumberOfParticipants();
			postRepository.save(post);
		} else {
			throw new CustomException(ErrorCode.FAIL_LEAVE_CHATROOM);
		}

		queryFactory.delete(qMemberChatRoom)
				.where(qMemberChatRoom.chatRoom.id.eq(id), qMemberChatRoom.member.id.eq(member.getId()))
				.execute();

		return ResponseEntity.ok(Message.setSuccess(StatusEnum.OK, "신청 취소 완료"));
	}
}
