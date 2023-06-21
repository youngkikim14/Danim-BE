package com.project.danim_be.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ChatRoomDto {
	private String roomName;
	private List<Map<String, Object>> userInfo;
}