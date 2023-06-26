package com.project.danim_be.chat.service;


import com.project.danim_be.chat.controller.ChatController;
import com.project.danim_be.chat.dto.ChatDto;
import com.project.danim_be.chat.entity.ChatMessage;
import com.project.danim_be.chat.repository.ChatMessageRepository;
import com.project.danim_be.chat.repository.ChatRoomRepository;
import com.project.danim_be.chat.repository.MemberChatRoomRepository;
import com.project.danim_be.chat.service.ChatMessageService;
import com.project.danim_be.chat.service.ChatRoomService;
import com.project.danim_be.member.repository.MemberRepository;
import com.project.danim_be.notification.service.NotificationService;
import com.project.danim_be.post.repository.PostRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceTest {
	@Mock
	private MemberChatRoomRepository memberChatRoomRepository;
	@Mock
	private ChatMessageRepository chatMessageRepository;
	@Mock
	private ChatRoomRepository chatRoomRepository;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private PostRepository postRepository;
	@Mock
	private NotificationService notificationService;

	@InjectMocks
	private ChatMessageService chatMessageService;

	private ChatDto chatDto;

	@BeforeEach
	void setUp() {
		chatDto = ChatDto.builder()
			.type(ChatDto.MessageType.TALK)
			.roomName("TestRoom")
			.sender("TestSender")
			.message("TestMessage")
			.time(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
			.build();
	}

	@Test
	void testSendMessage() {
		// 여기에는 필요한 Mockito 스텁(stub)을 추가해주세요.
		// 예를 들어, chatRoomRepository.findByRoomName(roomName)이 호출될 때 적절한 ChatRoom 객체를 반환하도록 스텁을 설정하면 됩니다.

		chatMessageService.sendMessage(chatDto);

		// sendMessage 메서드가 chatMessageRepository.save() 메서드를 호출하는지 확인합니다.
		verify(chatMessageRepository, times(1)).save(any(ChatMessage.class));
	}
}



