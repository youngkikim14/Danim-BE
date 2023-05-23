package com.project.danim_be.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequestDto {
    private String userId;
    private String ageRange;
    private String gender;

    @Builder
    public MemberRequestDto(String email, String age, String gender) {
        this.userId = email;
        this.ageRange = age;
        this.gender = gender;
    }
}
