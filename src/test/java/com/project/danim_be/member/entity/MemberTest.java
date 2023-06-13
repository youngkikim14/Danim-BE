package com.project.danim_be.member.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@Nested
class MemberTest {

    @Test
    @DisplayName("멤버가 생성되는지 확인하는 테스트")
    void createMemberTest() {
        // given
        Member member = Member.builder().userId("limslki333@hanmail.net").nickname("우아한사자").provider("DANIM").gender("남").isDeleted(false).ageRange("30-39").build();
        // when, then
        Assertions.assertEquals("limslki333@hanmail.net", member.getUserId());
        Assertions.assertEquals("우아한사자", member.getNickname());
        Assertions.assertEquals("DANIM", member.getProvider());
        Assertions.assertEquals("남", member.getGender());
        Assertions.assertEquals(false, member.getIsDeleted());
        Assertions.assertEquals("30-39", member.getAgeRange());
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void signOutTest() {

        // given
        String userId = "user5555@gmail.com";
        String nickname = "우아한악어";
        String password = "test1410";
        Member member = new Member(userId, password, nickname);

        // when
        member.signOut();

        // then
        Assertions.assertEquals(true, member.getIsDeleted());
        Assertions.assertEquals("user5555@gmail.com(withdrawal)", member.getUserId());
        Assertions.assertEquals("우아한악어(withdrawal)", member.getNickname());
    }

}