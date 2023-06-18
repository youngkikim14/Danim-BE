package com.project.danim_be.chat.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.danim_be.chat.dto.ChatDto;
import com.project.danim_be.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage extends Timestamped implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String message;

	@Enumerated(EnumType.STRING)
	private ChatDto.MessageType type;
	private String chatRoomName;
	private String sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private ChatRoom chatRoom;

	public ChatMessage(ChatDto chatDto, ChatRoom chatRoom) {
		this.message = chatDto.getMessage();
		this.type = chatDto.getType();
		this.sender = chatDto.getSender();
		this.chatRoomName = chatRoom.getRoomName();
		this.chatRoom = chatRoom;
	}

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom=chatRoom;
	}
}
