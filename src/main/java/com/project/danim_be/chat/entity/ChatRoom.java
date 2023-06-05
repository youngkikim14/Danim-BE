package com.project.danim_be.chat.entity;

import com.project.danim_be.member.entity.Member;
import com.project.danim_be.post.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String roomId;					//방번호
	private Long adminMemberId;

	@OneToMany(mappedBy = "chatRoom")
	private List<MemberChatRoom> memberChatRoomList;

	@OneToOne
	private Post post;

	@OneToMany
	private List<ChatMessage> chatMessages;


}
