package com.project.danim_be.chat.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatRoomResponseDto {
	private String roomName;
	private Map<String,Object> userInfo;

	public ChatRoomResponseDto(String roomName, Map<String, Object> userInfo) {
		this.roomName = roomName;
		this.userInfo = userInfo;
	}
}
