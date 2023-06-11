package com.project.danim_be.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomConnectSuccessDto {

	private String roomName;

	List<String> nickName;



}
