
package com.project.danim_be.member.service;

import com.project.danim_be.common.exception.CustomException;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.member.dto.*;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;
import com.project.danim_be.post.dto.MypagePostResponseDto;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.repository.PostRepository;
import com.project.danim_be.security.jwt.JwtUtil;
import com.project.danim_be.security.jwt.TokenDto;
import com.project.danim_be.security.refreshToken.RefreshToken;
import com.project.danim_be.security.refreshToken.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@Nested
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private SignupRequestDto signupRequestDto;
    @Mock
    private CheckIdRequestDto checkIdRequestDto;
    @Mock
    private CheckNicknameRequestDto checkNicknameRequestDto;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private PostRepository postRepository;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signupTest() {
        // given
        signupRequestDto = new SignupRequestDto("limslki333@hanmail.net",
                "1234", "우아한고양이", "30대", "여", true, true);

        // when
        ResponseEntity<Message> result = memberService.signup(signupRequestDto);

        // then
        assertEquals(result.getBody().getMessage(), "회원 가입 성공");
    }

    @Test
    @DisplayName("userId 중복 예외 확인")
    void duplicateMemberTest() {
        // given
        checkIdRequestDto = new CheckIdRequestDto("limslki333@hanmail.net");
        checkIdRequestDto = new CheckIdRequestDto("limslki333@hanmail.net");

        // when, then
        try {
            memberService.checkId(checkIdRequestDto);
        } catch (CustomException e) {
            assertEquals("중복된 아이디 입니다.", e.getErrorCode().getDetail());
        }
    }

    @Test
    @DisplayName("닉네임 중복 예외 확인")
    void duplicateNicknameTest() {
        // given
        checkNicknameRequestDto = new CheckNicknameRequestDto("우아한사자");
        checkNicknameRequestDto = new CheckNicknameRequestDto("우아한사자");

        // when, then
        try {
            memberService.checkNickname(checkNicknameRequestDto);
        } catch (CustomException e) {
            assertEquals("중복된 닉네임 입니다.", e.getErrorCode().getDetail());
        }
    }


    @Test
    @DisplayName("로그인 정상 테스트")
    void login() {

        //given

        String userId = "test14@google.com";
        String password = "test14!!";
        TokenDto tokenDto = new TokenDto("Access", "Refresh");
        LoginRequestDto loginRequestDto = new LoginRequestDto(userId, password);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        Member member = new Member();

        when(memberRepository.findByUserId(userId))
                .thenReturn(Optional.of(member));
        when(passwordEncoder.matches(password, member.getPassword()))
                .thenReturn(true);
        when(jwtUtil.createAllToken(userId))
                .thenReturn(tokenDto);

        //when
        ResponseEntity<Message> response = memberService.login(loginRequestDto, mockResponse);

        //then
        assertEquals(response.getBody().getMessage(), "로그인 성공");

    }

    @Test
    @DisplayName("로그아웃 테스트")
    void logout() {

        // given
        Member logoutMember = new Member();
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        RefreshToken refreshToken = new RefreshToken();
        String accessToken = "accessToken";

        when(refreshTokenRepository.findByUserId(logoutMember.getUserId()))
                .thenReturn(Optional.of(refreshToken));
        httpServletRequest.addHeader("ACCESS_KEY", accessToken);

        //when
        ResponseEntity<Message> response = memberService.logout(logoutMember, httpServletRequest);

        // then
        assertEquals(response.getBody().getMessage(), "로그아웃 성공");

    }


    @Test
    @DisplayName("마이페이지 유저정보")
    void memberInfo() {

        //given
        Long memberpk = 2L;
        String memberId = "user5555@gmail.com";
        String memberNickName = "우아한악어";
        String memberPassword = "test1410";
        Long ownerpk = 3L;
        String ownerId = "user4444@gmail.com";
        String ownerNickName = "우아한코끼리";
        String ownerPassword = "test1410";
        Member member = new Member(memberId, memberPassword, memberNickName);
        Member owner = new Member(ownerId, ownerPassword, ownerNickName);

        when(memberRepository.findById(memberpk)).thenReturn(Optional.of(member));
        when(memberRepository.findById(ownerpk)).thenReturn(Optional.of(owner));

        //when
        ResponseEntity<Message> result = memberService.memberInfo(ownerpk, memberpk);

        //then
        assertEquals(result.getBody().getMessage(),"조회 성공");
        assertNotEquals(result.getBody().getData(),"우아한 코끼리");

    }

    @Test
    @DisplayName("마이페이지 게시물 정보")
    void memberPosts() {

        //given
        Long memberpk = 2L;
        String memberId = "user5555@gmail.com";
        String memberNickName = "우아한악어";
        String memberPassword = "test1410";
        Long ownerpk = 3L;
        String ownerId = "user4444@gmail.com";
        String ownerNickName = "우아한코끼리";
        String ownerPassword = "test1410";
        Member member = new Member(memberId, memberPassword, memberNickName);
        Member owner = new Member(ownerId, ownerPassword, ownerNickName);

        when(memberRepository.findById(memberpk)).thenReturn(Optional.of(member));
        when(memberRepository.findById(ownerpk)).thenReturn(Optional.of(owner));

        //when
        ResponseEntity<Message> result = memberService.memberPosts(ownerpk, memberpk);

        //then
        assertEquals(result.getBody().getMessage(),"조회 성공");
        assertEquals(result.getBody().getData(), validMember(owner, false));

    }

    private java.util.List<MypagePostResponseDto> validMember(Member member, Boolean owner) {
        List<Post> postList = postRepository.findAllByMemberOrderByCreatedAtDesc(member);
        List<MypagePostResponseDto> mypagePostResponseDtoList = new ArrayList<>();
        for (Post post : postList) {
            mypagePostResponseDtoList.add(new MypagePostResponseDto(post, owner));
        }
        return mypagePostResponseDtoList;
    }
}

