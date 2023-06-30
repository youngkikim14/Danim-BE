package com.project.danim_be.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.danim_be.chat.entity.ChatMessage;
import com.project.danim_be.chat.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatListResponseDto {

	private Long roomId;

	private String postTitle;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createAtTime;

	private String roomName;

	private ChatMessage lastMessage;

	public ChatListResponseDto(ChatRoom chatRoom) {

		this.roomId = chatRoom.getId();
		this.postTitle = chatRoom.getPost().getPostTitle();
		this.createAtTime = chatRoom.getPost().getCreatedAt();
		this.roomName = chatRoom.getRoomName();
		List<ChatMessage> chatMessages = chatRoom.getChatMessages();

		if (!chatMessages.isEmpty()) {
			chatMessages.sort(Comparator.comparing(ChatMessage::getCreatedAt).reversed());
			this.lastMessage = chatMessages.get(0);
		} else {
			this.lastMessage = null;
		}

	}
}
