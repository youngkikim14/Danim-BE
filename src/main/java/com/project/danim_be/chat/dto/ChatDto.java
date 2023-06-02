package com.project.danim_be.chat.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDto {

	public enum MessageType{
		ENTER,
		TALK,
		LEAVE
	}

	private MessageType type;
	private String roomId;
	private String sender;
	private String message;
	private LocalDateTime time;

}
