package com.project.danim_be.chat.controller;

import com.project.danim_be.chat.dto.ChatDto;
import com.project.danim_be.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

	private final SimpMessageSendingOperations template;
	private final ChatMessageService chatMessageService;

	// @MessageMapping
	// public void enterUser(){
	//
	// }


	// //채팅방 참여(웹소켓연결/방입장) == 매칭 신청 버튼
	// @PostMapping("")
	// public ResponseEntity<Message> joinChatRoom(@PathVariable("Post_id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
	// 	return chatMessageService.joinChatRoom(id, userDetails.getMember());
	// }
	//
	// //내가 쓴글의 채팅방 목록조회
	// @GetMapping("")
	// public ResponseEntity<Message> myChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails) {
	// 	return chatMessageService.myChatRoom(userDetails.getMember().getId());
	// }
	//
	//
	// //내가 신청한 채팅방 목록조회
	// @GetMapping("")
	// public ResponseEntity<Message> myJoinChatroom(@AuthenticationPrincipal UserDetailsImpl userDetails) {
	// 	return chatMessageService.myJoinChatroom(userDetails.getMember().getId());
	// }

	//메시지 보내기
	@MessageMapping("/chat/send")
	public void sendMessage(@Payload ChatDto chatDto){
		System.out.println(chatDto.getMessage());
		if(ChatDto.MessageType.TALK.equals(chatDto.getType()))
			chatDto.setMessage(chatDto.getSender()+"님이 입장하셧습니다.");
			template.convertAndSend("/sub/chat/room/" + chatDto.getRoomId(), chatDto);
		// chatMessageService.sendMessage(roomId,chatDto);
	}
/*
	@EventListener
	public void webSocketDisconnectListener(SessionDisconnectEvent event) {
		log.info("DisConnEvent {}", event);

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		// stomp 세션에 있던 uuid 와 roomId 를 확인해서 채팅방 유저 리스트와 room 에서 해당 유저를 삭제
		String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
		String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

		log.info("headAccessor {}", headerAccessor);

		// 채팅방 유저 -1
		repository.minusUserCnt(roomId);

		// 채팅방 유저 리스트에서 UUID 유저 닉네임 조회 및 리스트에서 유저 삭제
		String username = repository.getUserName(roomId, userUUID);
		repository.delUser(roomId, userUUID);

		if (username != null) {
			log.info("User Disconnected : " + username);

			// builder 어노테이션 활용
			ChatDTO chat = ChatDTO.builder()
				.type(ChatDTO.MessageType.LEAVE)
				.sender(username)
				.message(username + " 님 퇴장!!")
				.build();

			template.convertAndSend("/sub/chat/room/" + roomId, chat);
		}
	}

*/
	//추방하기


	//신청취소(나가기)

}
