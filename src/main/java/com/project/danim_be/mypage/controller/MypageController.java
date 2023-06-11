package com.project.danim_be.mypage.controller;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.mypage.dto.RequestDto.MypageRequestDto;
import com.project.danim_be.mypage.service.MypageService;
import com.project.danim_be.security.auth.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "MypageController", description = "Myppage API")
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    // 마이페이지 - 사용자 정보
    @Operation(summary = "마이페이지 사용자 정보 API", description = "마이페이지 사용자 정보")
    @GetMapping("/{ownerId}/info")
    public ResponseEntity<Message> memberInfo(@PathVariable Long ownerId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.memberInfo(ownerId, userDetails.getMember().getId());
    }

    // 마이페이지 - 게시물 목록
    @Operation(summary = "마이페이지 게시물 목록 API", description = "마이페이지 게시물 목록")
    @GetMapping("/{ownerId}/posts")
    public ResponseEntity<Message> memberPosts(@PathVariable Long ownerId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.memberPosts(ownerId, userDetails.getMember().getId());
    }

    @Operation(summary = "마이페이지 내가 받은 후기목록 API", description = "마이페이지 내가 받은 후기목록")
    @GetMapping("{ownerId}/review")
    public ResponseEntity<Message> memberReview(@PathVariable Long ownerId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.memberReview(ownerId, userDetails.getMember().getId());
    }

    //마이페이지 - 회원정보 수정
    @Operation(summary = "마이페이지 회원정보 수정 API", description = "마이페이지 회원정보 수정")
    @PutMapping(value = "{ownerId}/myInfo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> editMember(@PathVariable Long ownerId, @ModelAttribute MypageRequestDto mypageRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return mypageService.editMember(ownerId ,mypageRequestDto, userDetails.getMember());
    }
}
