package com.project.danim_be.chat.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageListDto {
    private Long id;

    private String message;

    private String sender;

    private LocalDateTime createdTime;


}
