package com.project.danim_be.chat.config;


import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.messaging.Message;
	import org.springframework.messaging.MessageChannel;
	import org.springframework.messaging.simp.stomp.StompCommand;
	import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
	import org.springframework.messaging.support.ChannelInterceptor;
	import org.springframework.messaging.support.MessageHeaderAccessor;
	import org.springframework.stereotype.Component;

import com.project.danim_be.chat.service.ChatMessageService;

@Component
public class SubscribeCheck implements ChannelInterceptor {

	@Autowired
	private ChatMessageService chatMessageService;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
			String destination = accessor.getDestination();
			// destination에서 userId를 파싱하고,
			Long userId = parseUserIdFromDestination(destination);
			chatMessageService.alarmList(userId);
		}
		return message;
	}

	private Long parseUserIdFromDestination(String destination) {
		String[] parts = destination.split("/");
		return Long.parseLong(parts[parts.length - 1]);
	}
}
