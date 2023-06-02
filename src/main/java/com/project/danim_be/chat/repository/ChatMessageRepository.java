package com.project.danim_be.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.danim_be.chat.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
