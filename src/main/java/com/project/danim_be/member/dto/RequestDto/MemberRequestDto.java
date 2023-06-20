package com.project.danim_be.member.dto.RequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequestDto {
    private String userId;

    @Builder
    public MemberRequestDto(String email) {
        this.userId = email;
    }
}
