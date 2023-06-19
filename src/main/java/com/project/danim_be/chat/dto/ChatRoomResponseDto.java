package com.project.danim_be.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.danim_be.chat.entity.ChatMessage;
import com.project.danim_be.chat.entity.ChatRoom;
import lombok.Getter;
import lombok.Setter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoomResponseDto {
	private String roomName;
	private Map<String,Object> userInfo;

	public ChatRoomResponseDto(String roomName, Map<String, Object> userInfo) {
		this.roomName = roomName;
		this.userInfo = userInfo;
	}
}
