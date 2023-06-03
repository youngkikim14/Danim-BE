package com.project.danim_be.chat.entity;

import com.project.danim_be.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class MemberChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "chatRoom_id")
	private ChatRoom chatRoom;
	/*
		member			chatRoom
		1					1
		2					1
		1					2
		3					1
	 */
}
