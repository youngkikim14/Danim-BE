package com.project.danim_be.member.dto.RequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequestDto {

    private String userId;

    private String userImage;

    @Builder
    public MemberRequestDto(String email, String userImage) {
        this.userId = email;
        this.userImage = userImage;
    }

}
