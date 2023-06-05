package com.project.danim_be.chat.entity;

import com.project.danim_be.member.entity.Member;
import com.project.danim_be.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

	@OneToOne(mappedBy = "chatRoom",cascade = CascadeType.ALL)
	private Post post;

	@OneToMany
	private List<ChatMessage> chatMessages;

	public void removeMember(Member imposter) {


	}
}
