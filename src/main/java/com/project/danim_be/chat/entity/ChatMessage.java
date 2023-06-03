package com.project.danim_be.chat.entity;

import com.project.danim_be.common.entity.Timestamped;
import jakarta.persistence.*;

@Entity
public class ChatMessage extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String message;

	@ManyToOne
	private ChatRoom chatRoom;

}
