package com.project.danim_be.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageListDto {

    private Long id;

    private String message;

    private String sender;

    private LocalDateTime createdTime;

}
