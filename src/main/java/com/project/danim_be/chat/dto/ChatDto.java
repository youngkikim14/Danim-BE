package com.project.danim_be.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
