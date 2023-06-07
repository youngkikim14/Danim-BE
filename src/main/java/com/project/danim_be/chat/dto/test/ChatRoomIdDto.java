package com.project.danim_be.chat.dto.test;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomIdDto {

	private String RoomId;

	public ChatRoomIdDto(String s) {
		this.RoomId=s;
	}


}
