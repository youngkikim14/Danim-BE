package com.project.danim_be.chat.entity;

import java.time.LocalDateTime;

import com.project.danim_be.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Member member;

	@ManyToOne
	private ChatRoom chatRoom;

	private LocalDateTime firstJoinRoom;	// 맨처음 채팅방에 들어온시간
	private LocalDateTime recentConnect;	// 최근 접속한시간
	private LocalDateTime recentDisConnect;	// 마지막으로 떠난시간 (채팅방 접속을끊은시간)(강퇴.신청취소아님)

	//setter
	public void setFirstJoinRoom(LocalDateTime now) {this.firstJoinRoom = now;}
	public void setRecentConnect(LocalDateTime now) {this.recentConnect = now;}
	public void setRecentDisConnect(LocalDateTime now) {this.recentDisConnect = now;}

	public MemberChatRoom(Member member, ChatRoom chatRoom) {
		this.member = member;
		this.chatRoom = chatRoom;
	}



	/*
		member			chatRoom
		1					1
		2					1
		1					2
		3					1
	 */
}
