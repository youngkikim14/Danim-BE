package com.project.danim_be.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MypageResponseDto {

    private String nickname;
    private String imageUrl;
    private String content;
    private Boolean owner;

    public MypageResponseDto(String nickname, String imageUrl, String content, Boolean owner) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.content = content;
        this.owner = owner;
    }
}
