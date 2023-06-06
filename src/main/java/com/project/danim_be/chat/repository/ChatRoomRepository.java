package com.project.danim_be.chat.repository;


import java.util.Optional;

import com.project.danim_be.chat.entity.ChatMessage;

import com.project.danim_be.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, QuerydslPredicateExecutor<ChatRoom> {
	Optional<ChatRoom> findByRoomId(String roomId);

	// Optional<ChatRoom> findByChatMessage(ChatMessage chatMessage);

	ChatRoom findByChatMessagesId(Long id);
}
