package com.project.danim_be.chat.config;


import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;


@Component
public class SubscribeCheck implements ChannelInterceptor {

	private ApplicationEventPublisher eventPublisher;

	public SubscribeCheck(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
			String destination = accessor.getDestination();
			// destination에서 userId를 파싱하고,
			Long userId = parseUserIdFromDestination(destination);
			// 이벤트 발행
			eventPublisher.publishEvent(new SubscriptionEvent(userId));
		}
		return message;
	}
	public class SubscriptionEvent {
		private Long userId;

		public SubscriptionEvent(Long userId) {
			this.userId = userId;
		}

		public Long getUserId() {
			return userId;
		}
	}
	private Long parseUserIdFromDestination(String destination) {
		String[] parts = destination.split("/");
		return Long.parseLong(parts[parts.length - 1]);
	}
}
