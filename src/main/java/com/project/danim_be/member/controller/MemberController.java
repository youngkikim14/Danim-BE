package com.project.danim_be.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.member.dto.*;
import com.project.danim_be.member.service.GoogleService;
import com.project.danim_be.member.service.KakaoService;
import com.project.danim_be.member.service.MemberService;
import com.project.danim_be.member.service.NaverService;
import com.project.danim_be.security.auth.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "MemberController", description = "멤버 API")
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final NaverService naverService;
	private final KakaoService kakaoService;
	private final GoogleService googleService;

	//일반 회원가입
	@Operation(summary = "일반 회원가입 API", description = "일반 회원가입")
	@PostMapping("/signup")
	public ResponseEntity<Message> signup(@Valid @RequestBody SignupRequestDto signupRequestDto){

		return memberService.signup(signupRequestDto);
	}

	//일반 회원가입 아이디 중복 검사
	@Operation(summary = "일반 회원가입 아이디 중복 검사 API", description = "아이디 중복 검사")
	@PostMapping("/checkId")
	public ResponseEntity<Message> checkId(@RequestBody CheckIdRequestDto checkIdRequestDto) {
		return memberService.checkId(checkIdRequestDto);
	}

	//일반 회원가입 닉네임 중복 검사
	@Operation(summary = "일반 회원가입 닉네임 중복 검사 API", description = "닉네임 중복 검사")
	@PostMapping("/checkNickname")
	public ResponseEntity<Message> checkNickname(@RequestBody CheckNicknameRequestDto checkNicknameRequestDto) {
		return memberService.checkNickname(checkNicknameRequestDto);
  }

	//회원가입시 랜덤 닉네임 생성
	@Operation(summary = "회원가입시 랜덤 닉네임 생성 API", description = "랜덤 닉네임 생성")
	@GetMapping("/randomNickname")
	public ResponseEntity<Message> nicknameCreate() {
		return memberService.nicknameCreate();
	}

	//로그인
	@Operation(summary = "일반 로그인 API", description = "일반 로그인")
	@PostMapping("/login")
	public ResponseEntity<Message> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response ){
		return memberService.login(requestDto,response);
	}

	//로그아웃
	@Operation(summary = "로그아웃 API", description = "로그아웃")
	@DeleteMapping("/logout")
	public ResponseEntity<Message> logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request){
		return memberService.logout(userDetails.getMember(), request);
	}

	//네이버 소셜 로그인
	@Operation(summary = "네이버 소셜 로그인 API", description = "네이버 소셜 로그인")
	@GetMapping("/naver/callback")
	public ResponseEntity<Message> naverLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
		return naverService.naverLogin(code, response);
	}

	//카카오 소셜 로그인
	@Operation(summary = "카카오 소셜 로그인 API", description = "카카오 소셜 로그인")
	@GetMapping("/kakao/callback")
	public ResponseEntity<Message> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
		System.out.println(code);
		return kakaoService.kakaoLogin(code,response);
	}

	//구글 소셜 로그인
	@Operation(summary = "구글 소셜 로그인 API", description = "구글 소셜 로그인")
	@GetMapping("/google/callback")
	public ResponseEntity<Message> googleLogin(@RequestParam String code, HttpServletResponse response) {
		// registrationId 이 부분은 소셜 로그인을 공통으로 구현할때 쓰임 .registrationId 값에 @RequestParam으로 kakao나 naver, google이 들어가면
		// 그에 맞는 프로퍼티스에서 값을 가져와 같은 메서드로 서로 다른 소셜 로그인 구현 가능
		System.out.println(code);
		return googleService.socialLogin(code, response);
	}

	// 소셜 로그인 시 추가 회원 정보 작성
	@Operation(summary = "추가 사용자 정보 작성 API", description = "추가 사용자 정보 작성")
	@PostMapping("/userInfo")
	public ResponseEntity<Message> addUserInfo(@RequestBody UserInfoRequestDto userInfoRequestDto) {
		return memberService.addUserInfo(userInfoRequestDto);
	}

	//회원 탈퇴
	@Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴")
	@DeleteMapping("/delete")
	public ResponseEntity<Message> signOut(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return memberService.signOut(userDetails.getMember());
	}

	// 마이페이지 - 사용자 정보
	@Operation(summary = "마이페이지 사용자 정보 API", description = "마이페이지 사용자 정보")
	@GetMapping("/{ownerId}/info")
	public ResponseEntity<Message> memberInfo(@PathVariable Long ownerId, @AuthenticationPrincipal UserDetailsImpl userDetails){
		return memberService.memberInfo(ownerId, userDetails.getMember().getId());
	}

	// 마이페이지 - 게시물 목록
	@Operation(summary = "마이페이지 게시물 목록 API", description = "마이페이지 게시물 목록")
	@GetMapping("/{ownerId}/posts")
	public ResponseEntity<Message> memberPosts(@PathVariable Long ownerId, @AuthenticationPrincipal UserDetailsImpl userDetails){
		return memberService.memberPosts(ownerId, userDetails.getMember().getId());
	}

	@Operation(summary = "마이페이지 내가 받은 후기목록 API", description = "마이페이지 내가 받은 후기목록")
	@GetMapping("{ownerId}/review")
	public ResponseEntity<Message> memberReview(@PathVariable Long ownerId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		return memberService.memberReview(ownerId, userDetails.getMember().getId());
	}

	//마이페이지 - 회원정보 수정
	@Operation(summary = "마이페이지 회원정보 수정 API", description = "마이페이지 회원정보 수정")
	@PutMapping(value = "{ownerId}/myInfo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Message> editMember(@PathVariable Long ownerId, @ModelAttribute MypageRequestDto mypageRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
		return memberService.editMember(ownerId ,mypageRequestDto, userDetails.getMember());
	}

}

