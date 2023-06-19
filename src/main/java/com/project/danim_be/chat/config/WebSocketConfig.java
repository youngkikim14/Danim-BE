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


	@Override
public void registerStompEndpoints(StompEndpointRegistry registry){
    registry.addEndpoint("/ws-stomp")
            .setAllowedOriginPatterns("http://localhost:8080", "http://jxy.me","http://localhost:3000","http://127.0.0.1:3000",
				      "http://project-danim.s3-website.ap-northeast-2.amazonaws.com","https://www.da-nim.com","https://da-nim.com")
            .withSockJS();
}

// 	public void registerStompEndpoints(StompEndpointRegistry registry ){
// 		// stomp 접속 주소 url => /ws-stomp
// 		registry.addEndpoint("/ws-stomp") // 연결될 엔드포인트
// 			.setAllowedOrigins("http://localhost:8080","http://localhost:63342")
// 			.withSockJS();
// 	}


	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		registry.enableSimpleBroker("/queue","/sub");	 //수신 메시지를 구독하는 요청 url => 즉 메시지 받을 때
		registry.setApplicationDestinationPrefixes("/pub");	 //송신 메시지를 발행하는 요청 url => 즉 메시지 보낼 때
	}
}
	// private final StompHandler stompHandler; // jwt 인증
	//
	//
	// @Override
	// public void configureClientInboundChannel(ChannelRegistration registration) {
	// 	registration.interceptors(stompHandler);
	// }


