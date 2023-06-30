package com.project.danim_be.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ChatRoomResponseDto {

	private String roomName;

	private List<Map<String, Object>> userInfo;

	private List<Object> chatRecord;

	public ChatRoomResponseDto(String roomName, List<Map<String, Object>> userInfo, List<Object> chatRecord) {
		this.roomName = roomName;
		this.userInfo = userInfo;
		this.chatRecord =  chatRecord;
	}

}
