package com.project.danim_be.chat.dto.test;

import com.project.danim_be.chat.entity.MemberChatRoom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomMemberInfoDto {

	private String member;
	private String kickMember;
	private String connect;
	private String disConnect;


	public ChatRoomMemberInfoDto(MemberChatRoom memberChatRoom){

		if(memberChatRoom.getKickMember() == null || !memberChatRoom.getKickMember()){
			this.member = memberChatRoom.getMember().getNickname();
		}else{
			this.kickMember = memberChatRoom.getMember().getNickname();
		}
		this.connect=memberChatRoom.getRecentConnect().toString();
		if(memberChatRoom.getRecentDisConnect()==null){
			this.disConnect=memberChatRoom.getRecentConnect().toString();
		}else {
			this.disConnect = memberChatRoom.getRecentDisConnect().toString();
		}

	}

}
