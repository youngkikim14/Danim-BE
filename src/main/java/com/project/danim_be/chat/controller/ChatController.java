package com.project.danim_be.chat.controller;

import com.project.danim_be.chat.dto.ChatDto;
import com.project.danim_be.chat.service.ChatMessageService;
import com.project.danim_be.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

	@Autowired
	private SimpMessagingTemplate template;
	@Autowired
	private  SimpMessageSendingOperations messagingTemplate;
	private final ChatMessageService chatMessageService;
	private final ChatRoomService chatRoomService;

	// 메시지가왔을때 실행
	@MessageMapping("/chat/message")
	public void message(@Payload ChatDto chatDto, Principal principal){

		switch (chatDto.getType()) {

			case TALK -> {
				System.out.println("TYPE : TALK");
				String message = chatDto.getSender();
				message += " : ";
				message += chatDto.getMessage();
				chatMessageService.sendMessage(chatDto);

				messagingTemplate.convertAndSend("/sub/chat/room/" + chatDto.getRoomId(), message);
			}

			case ENTER -> {
				System.out.println("TYPE : ENTER");
				chatMessageService.visitMember(chatDto);

				ChatDto message = ChatDto.builder()
					.type(ChatDto.MessageType.ENTER)
					.roomId(chatDto.getRoomId())
					.sender(chatDto.getSender())
					.message(chatDto.getSender() + "님이 입장하셨습니다.")
					.build();
				messagingTemplate.convertAndSend("/sub/chat/room/" + chatDto.getRoomId(), message);
			}

			case LEAVE -> {
				System.out.println("TYPE : LEAVE");
				chatMessageService.leaveChatRoom(chatDto);
				//SSE요청시작!
				ChatDto leaveMessage = ChatDto.builder()
					.type(ChatDto.MessageType.LEAVE)
					.roomId(chatDto.getRoomId())
					.sender(chatDto.getSender())
					.message(chatDto.getSender() + "님이 접속을 끊었습니다.")
					.build();

				template.convertAndSend("/sub/chat/room/" + chatDto.getRoomId(), leaveMessage);
			}
			case KICK ->{

				chatMessageService.kickMember(chatDto);

				ChatDto kickMessage = ChatDto.builder()
					.type(ChatDto.MessageType.KICK)
					.roomId(chatDto.getRoomId())
					.sender(chatDto.getSender())   // 강퇴 담당자
					.message(chatDto.getSender() + "님이 " + "a" + "을(를) 강퇴하였습니다.")   // 강퇴 대상자
					.build();
				messagingTemplate.convertAndSend("/sub/chat/room/" + chatDto.getRoomId(), kickMessage);
			}
		}

		}

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


	//추방하기


	//신청취소(나가기)
	//=================================================================================================================================


