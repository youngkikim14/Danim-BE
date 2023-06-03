package com.project.danim_be.chat.controller;

import java.security.Principal;

import com.project.danim_be.chat.dto.ChatDto;
import com.project.danim_be.chat.service.ChatMessageService;
import com.project.danim_be.chat.service.ChatRoomService;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.security.auth.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ChatController {

	@Autowired
	private SimpMessagingTemplate template;
	private final SimpMessageSendingOperations operations;
	private final ChatMessageService chatMessageService;
	private final ChatRoomService chatRoomService;

	//채팅방입장
	@MessageMapping("/chat/enter")
	public void enterChatRoom(@Payload ChatDto chatDto, Principal principal	) {
		System.out.println("Enter");
		System.out.println(chatDto.getType());
		System.out.println(chatDto.getSender());
		System.out.println("chatDto.getRoomId() = " + chatDto.getRoomId());
		chatMessageService.visitMember(chatDto);

		ChatDto message = ChatDto.builder()
			.type(ChatDto.MessageType.ENTER)
			.roomId(chatDto.getRoomId())
			.sender(chatDto.getSender())
			.message(chatDto.getSender() + "님이 입장하셨습니다.")
			.build();

		template.convertAndSend("/pub/chat/room/" + chatDto.getRoomId(), message);
	}

	//메시지 보내기
	@MessageMapping("/chat/send")
	public void sendMessage(@Payload ChatDto chatDto,Principal principal) {
		System.out.println("Talk");
		System.out.println(chatDto.getMessage());

		chatMessageService.sendMessage(chatDto);

		operations.convertAndSend("/pub/chat/room/" + chatDto.getRoomId(), chatDto);
	}

	//채팅방 나가기
	@MessageMapping("/chat/leave")
	public void leaveChatRoom(@Payload ChatDto chatDto, Principal principal) {
		String sender = principal.getName();
		String roomId = chatDto.getRoomId();

		chatMessageService.leaveChatRoom(sender, roomId);

		ChatDto leaveMessage = ChatDto.builder()
			.type(ChatDto.MessageType.LEAVE)
			.roomId(chatDto.getRoomId())
			.sender(chatDto.getSender())
			.message(chatDto.getSender() + "님이 퇴장하셨습니다.")
			.build();

		template.convertAndSend("/pub/chat/room/" + chatDto.getRoomId(), leaveMessage);

	}

//=================================================================================================================================
	//채팅방 참여(웹소켓연결/방입장) == 매칭 신청 버튼
	// @PostMapping("")
	// public ResponseEntity<Message> joinChatRoom(@PathVariable("Post_id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
	// 	return chatRoomService.joinChatRoom(id, userDetails.getMember());
	// }
	//
	// //내가 쓴글의 채팅방 목록조회
	// @GetMapping("")
	// public ResponseEntity<Message> myChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails) {
	// 	return chatRoomService.myChatRoom(userDetails.getMember().getId());
	// }
	//
	//
	// //내가 신청한 채팅방 목록조회
	// @GetMapping("")
	// public ResponseEntity<Message> myJoinChatroom(@AuthenticationPrincipal UserDetailsImpl userDetails) {
	// 	return chatRoomService.myJoinChatroom(userDetails.getMember().getId());
	// }
	//
	// //메시지 보내기
	// @MessageMapping("/chat/send")
	// public void sendMessage(@Payload ChatDto chatDto){
	// 	System.out.println(chatDto.getMessage());
	// 	if(ChatDto.MessageType.TALK.equals(chatDto.getType()))
	// 		chatDto.setMessage(chatDto.getSender()+"님이 입장하셧습니다.");
	// 		template.convertAndSend("/sub/chat/room/" + chatDto.getRoomId(), chatDto);
	// 	// chatMessageService.sendMessage(roomId,chatDto);
	// }

	//추방하기


	//신청취소(나가기)
	//=================================================================================================================================

}
