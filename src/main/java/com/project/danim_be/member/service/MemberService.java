package com.project.danim_be.member.service;

import com.project.danim_be.chat.entity.MemberChatRoom;
import com.project.danim_be.chat.repository.MemberChatRoomRepository;
import com.project.danim_be.common.exception.CustomException;
import com.project.danim_be.common.exception.ErrorCode;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.RandomNickname;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.member.dto.RequestDto.*;
import com.project.danim_be.member.dto.ResponseDto.LoginResponseDto;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;
import com.project.danim_be.notification.service.NotificationService;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.repository.PostRepository;
import com.project.danim_be.security.jwt.JwtUtil;
import com.project.danim_be.security.jwt.TokenDto;
import com.project.danim_be.security.refreshToken.RefreshToken;
import com.project.danim_be.security.refreshToken.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.project.danim_be.common.exception.ErrorCode.FAIL_SIGNOUT;
import static com.project.danim_be.common.exception.ErrorCode.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenRepository refreshTokenRepository;
	private final PostRepository postRepository;
	private final MemberChatRoomRepository memberChatRoomRepository;
	private final JwtUtil jwtUtil;
	private final SocialService socialService;
	private final RandomNickname randomNickname;

	//회원가입
	@Transactional
	public ResponseEntity<Message> signup(SignupRequestDto signupRequestDto) {

		String userId = signupRequestDto.getUserId();
		String password = passwordEncoder.encode(signupRequestDto.getPassword());
		String nickname = signupRequestDto.getNickname();
		String ageRange = signupRequestDto.getAgeRange();
		String gender = signupRequestDto.getGender();

		Boolean agreeForGender = signupRequestDto.getAgreeForGender();
		Boolean agreeForAge = signupRequestDto.getAgreeForAge();

		if(memberRepository.findByUserId(userId).isPresent()){
			throw new CustomException(ErrorCode.DUPLICATE_IDENTIFIER);
		}
		if(memberRepository.findByNickname(nickname).isPresent()){
			throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
		}

		Member member = Member.builder()
				.userId(userId)
				.ageRange(ageRange)
				.gender(gender)
				.nickname(nickname)
				.password(password)
				.agreeForGender(agreeForGender)
				.agreeForAge(agreeForAge)
				.provider("DANIM")
				.isDeleted(false)
				.score(20.0)
				.imageUrl("https://danimdata.s3.ap-northeast-2.amazonaws.com/avatar.png")
				.build();

		memberRepository.save(member);

		Message message = Message.setSuccess(StatusEnum.OK,"회원 가입 성공");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	//일반 회원가입 아이디 중복 검사
	public ResponseEntity<Message> checkId(CheckIdRequestDto checkIdRequestDto) {

		if(memberRepository.findByUserId(checkIdRequestDto.getUserId()).isPresent()) {
			throw new CustomException(ErrorCode.DUPLICATE_IDENTIFIER);
		} else {
			Message message = Message.setSuccess(StatusEnum.OK,"아이디 중복 검사 성공");
			return new ResponseEntity<>(message, HttpStatus.OK);
		}
	}

	//일반 회원가입 닉네임 중복 검사
	public ResponseEntity<Message> checkNickname(CheckNicknameRequestDto checkNicknameRequestDto) {
		if(memberRepository.findByNickname(checkNicknameRequestDto.getNickname()).isPresent()) {
			throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
		} else {
			Message message = Message.setSuccess(StatusEnum.OK,"닉네임 중복 검사 성공");
			return new ResponseEntity<>(message, HttpStatus.OK);
		}
  }

	//랜덤 닉네임 생성
	@Transactional
	public ResponseEntity<Message> nicknameCreate() {
		String nickname = randomNickname.getRandomNickname();
		return ResponseEntity.ok(Message.setSuccess(StatusEnum.OK, "랜덤 닉네임 생성완료", nickname));
	}

	//소셜 로그인 시 추가 회원 정보 작성
	@Transactional
	public ResponseEntity<Message> addUserInfo(UserInfoRequestDto userInfoRequestDto) {
		Member member = memberRepository.findById(userInfoRequestDto.getUserId()).orElseThrow(
				() -> new CustomException(ErrorCode.ID_NOT_FOUND)
		);

		member.update(userInfoRequestDto);

		// SseEmitter sseEmitter = notificationService.connectNotification(member.getId());
		LoginResponseDto loginResponseDto =new LoginResponseDto(member);
		return ResponseEntity.ok(Message.setSuccess(StatusEnum.OK, "로그인 성공", loginResponseDto));
	}

	// 로그인
	private static final Object loginLock = new Object(); //동시성 제어를 위하여 동기처리를 위한 로직
	@Transactional
	public ResponseEntity<Message> login(LoginRequestDto requestDto, HttpServletResponse response) {
		String userId = requestDto.getUserId();
		String password = requestDto.getPassword();

		Member member;

		synchronized (loginLock) {
			 member = memberRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));
		}

		TokenDto tokenDto = jwtUtil.createAllToken(userId);

		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}

		Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(member.getUserId());
		if (refreshToken.isPresent()) {
			refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
		} else {
			RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), member.getUserId(), "DANIM");
			refreshTokenRepository.save(newToken);
		}

		setHeader(response, tokenDto);

		LoginResponseDto loginResponseDto = new LoginResponseDto(member);
		Message message = Message.setSuccess(StatusEnum.OK, "로그인 성공", loginResponseDto);

		return new ResponseEntity<>(message, HttpStatus.OK);
	}


	//로그아웃
	@Transactional
	public ResponseEntity<Message> logout(Member member, HttpServletRequest request) {
		refreshTokenRepository.existsByUserId(member.getUserId());

//		String accessToken = request.getHeader("ACCESS_KEY").substring(7);
		if(refreshTokenRepository.existsByUserId(member.getUserId())){
//			Long tokenTime = jwtUtil.getExpirationTime(accessToken);
			refreshTokenRepository.deleteByUserIdAndProvider(member.getUserId(), "DANIM");
			Message message = Message.setSuccess(StatusEnum.OK,"로그아웃 성공", member.getUserId());
			return new ResponseEntity<>(message, HttpStatus.OK);
		}
		throw new CustomException(USER_NOT_FOUND);
	}

	//회원 탈퇴
	@Transactional
	public ResponseEntity<Message> signOut(Member member) {
		member = memberRepository.findById(member.getId()).orElseThrow(() -> new CustomException(USER_NOT_FOUND));


		List<Post> posts = postRepository.findByMember_Id(member.getId());

		//채팅방입장삭제
		List<MemberChatRoom> memberChatRooms  =memberChatRoomRepository.findAllByMember_Id(member.getId());
		memberChatRoomRepository.deleteAll(memberChatRooms);
		//게시물삭제

		for(Post post : posts){
			post.decNumberOfParticipants();
			post.delete();
		}


		if(!member.getProvider().equals("DANIM")) {
			try {
				socialService.naverSignout(member);
			} catch (IOException e) {
				throw new CustomException(FAIL_SIGNOUT);
			}
		} else {
			try {
				refreshTokenRepository.delete(refreshTokenRepository.findByUserId(member.getUserId()).get());
			} catch (IncorrectResultSizeDataAccessException e) {
				throw new CustomException(FAIL_SIGNOUT);
			}

		}

		member.signOut();
		return ResponseEntity.ok(Message.setSuccess(StatusEnum.OK, "탈퇴 성공"));
	}

	//헤더 셋팅
	private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
		response.addHeader(JwtUtil.ACCESS_KEY, tokenDto.getAccessToken());
		response.addHeader(JwtUtil.REFRESH_KEY, tokenDto.getRefreshToken());
	}

	@Transactional
	public ResponseEntity<Message> refreshAccessToken(HttpServletRequest httpServletRequest, HttpServletResponse response) {

		String refresh_token = jwtUtil.resolveToken(httpServletRequest, JwtUtil.REFRESH_KEY);

		if (!jwtUtil.refreshTokenValid(refresh_token)) {

			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}

		String userId = jwtUtil.getUserInfoFromToken(refresh_token);

		String newAccessToken = jwtUtil.createToken(userId, "Access");

		RefreshToken foundRefreshToken = refreshTokenRepository.findByRefreshToken("Bearer " + refresh_token).orElseThrow(
			NoSuchElementException::new
		);

		RefreshToken updatedRefreshToken = foundRefreshToken.updateToken(newAccessToken);
		refreshTokenRepository.save(updatedRefreshToken);

		jwtUtil.setHeaderAccessToken(response, newAccessToken);

		Message message = Message.setSuccess(StatusEnum.OK, "액세스 토큰 재발급 성공");

		return new ResponseEntity<>(message, HttpStatus.OK);
	}



}
