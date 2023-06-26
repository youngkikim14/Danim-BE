package com.project.danim_be.chat.handler;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import com.project.danim_be.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

	private final JwtUtil jwtTokenProvider;

	// websocket을 통해 들어온 요청이 처리 되기전 실행된다.
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		// websocket 연결시 헤더의 jwt token 검증
		if (StompCommand.CONNECT == accessor.getCommand()) {
			String token = accessor.getFirstNativeHeader("ACCESS_KEY").substring(7);
			if (token != null) {
				jwtTokenProvider.validateToken(token);
			} else {
				System.out.println("Token is null in the header");
			}
		}

		return message;
	}


}
