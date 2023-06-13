package com.project.danim_be.member.dto;

import com.project.danim_be.member.dto.RequestDto.SignupRequestDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Nested
class SignupRequestDtoTest {

    @Test
    void createSignupRequestDtoTest() {
        // given
        String userId = "limslki333@hanmail.net";
        String password = "1234";
        String nickname = "우아한사자";
        String ageRange = "30대";
        String gender = "남";
        boolean agreeAgeRange = true;
        boolean agreeGender = true;

        // when
        SignupRequestDto signupRequestDto = new SignupRequestDto(userId, password, nickname, ageRange, gender, agreeGender, agreeAgeRange);

        // then
        assertEquals(userId, signupRequestDto.getUserId());
        assertEquals(password, signupRequestDto.getPassword());
        assertEquals(nickname, signupRequestDto.getNickname());
        assertEquals(ageRange, signupRequestDto.getAgeRange());
        assertEquals(gender, signupRequestDto.getGender());
        assertEquals(agreeGender, signupRequestDto.isAgreeForGender());
        assertEquals(agreeAgeRange, signupRequestDto.isAgreeForAge());
    }

}
