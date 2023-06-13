
package com.project.danim_be.member.service;

import com.project.danim_be.common.exception.CustomException;
import com.project.danim_be.common.exception.ErrorCode;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.member.dto.RequestDto.CheckIdRequestDto;
import com.project.danim_be.member.dto.RequestDto.CheckNicknameRequestDto;
import com.project.danim_be.member.dto.RequestDto.LoginRequestDto;
import com.project.danim_be.member.dto.RequestDto.SignupRequestDto;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;
import com.project.danim_be.notification.service.NotificationService;
import com.project.danim_be.post.repository.PostRepository;
import com.project.danim_be.security.jwt.JwtUtil;
import com.project.danim_be.security.jwt.TokenDto;
import com.project.danim_be.security.refreshToken.RefreshToken;
import com.project.danim_be.security.refreshToken.RefreshTokenRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
    @Mock
    private NotificationService notificationService;

    private static ValidatorFactory validatorFactory;
    private static Validator validator;



    @Test
    @DisplayName("회원가입 성공 테스트")
    void signupTest() {

        // given
        signupRequestDto = new SignupRequestDto("limslki333@hanmail.net",
                "1234!!as", "우아한고양이", "30대", "남", true, true);

        // when
        ResponseEntity<Message> result = memberService.signup(signupRequestDto);

        // then
        assertEquals(result.getBody().getMessage(), "회원 가입 성공");

    }

    @BeforeAll
    public static void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 유효성 검사")
    void signupFailTest3() {

        // given
        signupRequestDto = new SignupRequestDto("limslki333",
                "12", "슬기", "30대", "남", true, true);

        // when
        Set<ConstraintViolation<SignupRequestDto>> validate = validator.validate(signupRequestDto);

        Iterator<ConstraintViolation<SignupRequestDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<SignupRequestDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }

        // then
        assertThat(messages)
                .contains("아이디는 올바른 이메일 형식으로 입력해주세요. (ex-danim999@naver.com)",
                        "닉네임은 3~8자 이내 한글or영어(대소문자),숫자(선택) 범위에서 입력해주세요. 특수문자는 포함할 수 없습니다.",
                        "비밀번호는 5~12자 이내 영어(소문자),숫자,특수기호(선택) 범위에서 입력해야합니다.");

    }

    @Test
    @DisplayName("userId 중복 예외 확인")
    void duplicateMemberTest() {

        // given
        checkIdRequestDto = new CheckIdRequestDto("limslki333@hanmail.net");
        String userID = "limslki333@hanmail.net";

        // when
        when(memberRepository.findByUserId(userID))
                .thenThrow(new CustomException(ErrorCode.DUPLICATE_IDENTIFIER));
        // then
        CustomException e = assertThrows(CustomException.class, () -> {memberService.checkId(checkIdRequestDto);});
        assertEquals("중복된 아이디 입니다.", e.getErrorCode().getDetail());

    }

    @Test
    @DisplayName("닉네임 중복 예외 확인")
    void duplicateNicknameTest() {

        // given
        checkNicknameRequestDto = new CheckNicknameRequestDto("우아한사자");
        String nickname = "우아한사자";

        // when
        when(memberRepository.findByNickname(nickname))
                .thenThrow(new CustomException(ErrorCode.DUPLICATE_NICKNAME));

        // then
        CustomException e = assertThrows(CustomException.class, () -> {memberService.checkNickname(checkNicknameRequestDto);});
        assertEquals("중복된 닉네임 입니다.", e.getErrorCode().getDetail());

    }

    @Test
    @DisplayName("로그인 정상 테스트")
    void loginTest() {

        //given
        String userId = "test14@google.com";
        String password = "test14!!";
        TokenDto tokenDto = new TokenDto("Access", "Refresh");
        LoginRequestDto loginRequestDto = new LoginRequestDto(userId, password);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        Member member = new Member();

        //when
        when(memberRepository.findByUserId(userId))
                .thenReturn(Optional.of(member));
        when(passwordEncoder.matches(password, member.getPassword()))
                .thenReturn(true);
        when(jwtUtil.createAllToken(userId))
                .thenReturn(tokenDto);

        ResponseEntity<Message> response = memberService.login(loginRequestDto, mockResponse);

        //then
        assertEquals(response.getBody().getMessage(), "로그인 성공");

    }

    @Test
    @DisplayName("로그인 실패 테스트 - 등록되지 않은 아이디")
    void loginFailTest() {

        // given
        String userId = "test@google.com";
        String password = "test14!!";
        LoginRequestDto loginRequestDto = new LoginRequestDto(userId, password);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        //when
        when(memberRepository.findByUserId(userId))
                .thenThrow(new CustomException(ErrorCode.ID_NOT_FOUND));

        // then
        CustomException e = assertThrows(CustomException.class, () -> {memberService.login(loginRequestDto, mockResponse);});
        assertEquals("등록되지 않은 아이디 입니다.", e.getErrorCode().getDetail());

    }

    @Test
    @DisplayName("로그인 실패 테스트 - 비밀번호 불일치")
    void loginFailTest2() {

        // given
        String userId = "test14@google.com";
        String password = "test";
        LoginRequestDto loginRequestDto = new LoginRequestDto(userId, password);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        Member member = new Member();

        // when
        when(memberRepository.findByUserId(userId))
                .thenReturn(Optional.of(member));
        when(passwordEncoder.matches(password, member.getPassword()))
                .thenReturn(false);

        // then
        CustomException e = assertThrows(CustomException.class, () -> {memberService.login(loginRequestDto, mockResponse);});
        assertEquals("잘못된 비밀번호 입니다.", e.getErrorCode().getDetail());

    }

    @Test
    @DisplayName("로그아웃 테스트")
    void logoutTest() {

        // given
        Member logoutMember = new Member();
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        RefreshToken refreshToken = new RefreshToken();
        String accessToken = "accessToken";

        // when
        when(refreshTokenRepository.findByUserId(logoutMember.getUserId()))
                .thenReturn(Optional.of(refreshToken));
        httpServletRequest.addHeader("ACCESS_KEY", accessToken);

        ResponseEntity<Message> response = memberService.logout(logoutMember, httpServletRequest);

        // then
        assertEquals(response.getBody().getMessage(), "로그아웃 성공");

    }

}

