package com.project.danim_be.review.controller;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.review.dto.ReviewRequestDto;
import com.project.danim_be.review.service.ReviewService;
import com.project.danim_be.security.auth.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ReviewController", description = "여행 리뷰 - 댓글 API")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성 API", description = "리뷰 작성")
    @PostMapping("api/post/{postId}/review")
    public ResponseEntity<Message> createReview(@PathVariable Long postId, @RequestBody ReviewRequestDto reviewRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.createReview(postId, reviewRequestDto, userDetails.getMember());
    }
}
