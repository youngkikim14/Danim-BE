package com.project.danim_be.chat.dto;


import com.project.danim_be.chat.entity.MemberChatRoom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomDto {
    private String member;

    public ChatRoomDto(MemberChatRoom memberChatRoom) {
        this.member = memberChatRoom.getMember().getNickname();
    }
}
