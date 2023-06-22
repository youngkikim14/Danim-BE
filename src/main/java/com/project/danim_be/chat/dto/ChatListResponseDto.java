package com.project.danim_be.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.danim_be.chat.entity.ChatMessage;
import com.project.danim_be.chat.entity.ChatRoom;
import com.project.danim_be.chat.entity.MemberChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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
	private Date createAtTime;
	private String roomName;
	private ChatMessage lastMessage;
	private List<String> imageUrls;


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
		List<MemberChatRoom> memberChatRoomList = chatRoom.getMemberChatRoomList();
		List<String> imageUrls = new ArrayList<>();
		for (MemberChatRoom memberChatRoom : memberChatRoomList) {
			String imageUrl = memberChatRoom.getMember().getImageUrl();
			imageUrls.add(imageUrl);
		}
		this.imageUrls = imageUrls;

	}
}
