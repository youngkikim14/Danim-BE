package com.project.danim_be.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


// import com.project.danim_be.chat.handler.StompHandler;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	// private final StompHandler stompHandler; // jwt 인증
	//
	//
	// @Override
	// public void configureClientInboundChannel(ChannelRegistration registration) {
	// 	registration.interceptors(stompHandler);
	// }


	@Override
public void registerStompEndpoints(StompEndpointRegistry registry){
    registry.addEndpoint("/ws-stomp")
            .setAllowedOriginPatterns("http://localhost:8080", "http://jxy.me")
            .withSockJS();
}


	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		registry.enableSimpleBroker("/queue","/sub");	 //수신 메시지를 구독하는 요청 url => 즉 메시지 받을 때
		registry.setApplicationDestinationPrefixes("/pub");	 //송신 메시지를 발행하는 요청 url => 즉 메시지 보낼 때
	}
}
