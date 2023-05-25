package com.project.danim_be.member.service;

import com.project.danim_be.common.exception.CustomException;
import com.project.danim_be.member.dto.SignupRequestDto;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Nested
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;
    @Mock
    private Member member;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    MemberRepository memberRepository;
    @Mock
    private SignupRequestDto signupRequestDto;

//    @Test
//    @DisplayName("회원가입 테스트")
//    void signupTest() {
//        // given
//        signupRequestDto = new SignupRequestDto("limslki333@hanmail.net",
//                "1234", "우아한사자", "30-39");
//
//        // when
//        memberService.signup(signupRequestDto);
//
//        // then
//        assertEquals(member.getUserId(), signupRequestDto.getUserId());
//        assertEquals(member.getPassword(), signupRequestDto.getPassword());
//        assertEquals(member.getNickname(), signupRequestDto.getNickname());
//        assertEquals(member.getAgeRange(), signupRequestDto.getAgeRange());
//    }

    @Test
    @DisplayName("userId 중복 예외 확인")
    void duplicateMemberTest() {
        // given
        signupRequestDto = new SignupRequestDto("limslki333@hanmail.net",
                "1234", "우아한사자", "30-39");
        signupRequestDto = new SignupRequestDto("limslki333@hanmail.net",
                "4321", "용감한토끼", "20-29");

        // when, then
        try {
            memberService.signup(signupRequestDto);
        } catch (CustomException e) {
            assertEquals("사용자 등록 오류입니다.", e.getMessage());
        }
    }

    @Test
    @DisplayName("닉네임 중복 예외 확인")
    void duplicateNicknameTest() {
        // given
        signupRequestDto = new SignupRequestDto("limslki333@hanmail.net",
                "1234", "우아한사자", "30-39");
        signupRequestDto = new SignupRequestDto("limseulki333@gmail.com",
                "4321", "우아한사자", "20-29");

        // when, then
        try {
            memberService.signup(signupRequestDto);
        } catch (CustomException e) {
            assertEquals("사용자 등록 오류입니다.", e.getMessage());
        }
    }

}