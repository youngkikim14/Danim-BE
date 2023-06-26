package com.project.danim_be.chat.controller;

import static org.mockito.Mockito.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.project.danim_be.chat.dto.ChatDto;
import com.project.danim_be.chat.service.ChatMessageService;

@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {
	@Mock
	private ChatMessageService chatMessageService;
	@Mock
	private SimpMessageSendingOperations messagingTemplate;
	@Mock
	Principal principal;

	@InjectMocks
	private ChatController chatController;

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

		when(principal.getName()).thenReturn(chatDto.getSender());
	}

	@Test
	void testMessage() throws Exception {
		chatController.message(chatDto, principal);

		verify(chatMessageService, times(1)).sendMessage(chatDto);
		verify(messagingTemplate, times(1)).convertAndSend(
			"/sub/chat/room/" + chatDto.getRoomName(), any(ChatDto.class));
	}
}