package com.project.danim_be.chat.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.project.danim_be.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ChatRoom implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String roomName;

	private Long adminMemberId;

	@OneToMany(mappedBy = "chatRoom")
	private List<MemberChatRoom> memberChatRoomList;

	@OneToOne
	@JoinColumn(name = "post_id")
	private Post post;

	@OneToMany(mappedBy = "chatRoom")
	private List<ChatMessage> chatMessages;

}
