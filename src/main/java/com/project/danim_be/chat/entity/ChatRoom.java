package com.project.danim_be.chat.entity;

import java.util.List;

import com.project.danim_be.post.entity.Post;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String roomId;					//방번호

	@OneToMany(mappedBy = "chatRoom")
	private List<MemberChatRoom> memberChatRoomList;

	@OneToOne
	private Post post;

	@OneToMany
	private List<ChatMessage> chatMessages;

}
