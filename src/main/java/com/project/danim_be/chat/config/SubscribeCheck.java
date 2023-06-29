package com.project.danim_be.chat.config;


import com.project.danim_be.chat.entity.MemberChatRoom;
import com.project.danim_be.chat.repository.MemberChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SubscribeCheck implements ChannelInterceptor {
	private ApplicationEventPublisher eventPublisher;
	private Map<String, Long> sessionUserMap = new HashMap<>();
	@Autowired
	private MemberChatRoomRepository memberChatRoomRepository;

	public SubscribeCheck(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			System.out.println("STOMP 연결" + accessor.getSessionId());
		} else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
			String destination = accessor.getDestination();

			if (destination != null && destination.startsWith("/sub/alarm/")) {
				Long userId = parseUserIdFromDestination(destination);
				sessionUserMap.put(accessor.getSessionId(), userId);
				eventPublisher.publishEvent(new SubscriptionEvent(userId));
			}
		}else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
			Long userId = sessionUserMap.get(accessor.getSessionId());
			allLeave(userId);
			System.out.println("User " + userId + " disconnected.");

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

	//올리브?
	@Transactional
	public void allLeave(Long userId){
		List<MemberChatRoom> memberChatRoomList = memberChatRoomRepository.findAllByMember_Id(userId);
		log.info(" memberChatRoomList :  {}",memberChatRoomList);
		for(MemberChatRoom memberChatRoom : memberChatRoomList){
			log.info(" getId :  {}",memberChatRoom.getMember().getId());
			memberChatRoom.setRecentDisConnect(LocalDateTime.now());
			memberChatRoomRepository.save(memberChatRoom);
		}
	}
	private Long parseUserIdFromDestination(String destination) {
		String[] parts = destination.split("/");
		return Long.parseLong(parts[parts.length - 1]);
	}
}
