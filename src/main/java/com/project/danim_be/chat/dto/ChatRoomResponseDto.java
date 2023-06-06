package com.project.danim_be.chat.dto;

import com.project.danim_be.chat.entity.ChatRoom;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatRoomResponseDto {

	private Long id;

	private String postTitle;

	private LocalDateTime createdTime;

	public ChatRoomResponseDto(ChatRoom chatRoom) {
		this.id = chatRoom.getId();
		this.postTitle = chatRoom.getPost().getPostTitle();
		this.createdTime = chatRoom.getPost().getCreatedAt();
	}
}
