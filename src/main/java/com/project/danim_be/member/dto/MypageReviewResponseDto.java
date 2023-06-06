package com.project.danim_be.member.dto;

import com.project.danim_be.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MypageReviewResponseDto {

    private String userId;
    private Double point;
    private String review;
    private LocalDateTime createdAt;

    public MypageReviewResponseDto(Review receiveReview) {
        this.userId = receiveReview.getReview();
        this.point = receiveReview.getPoint();
        this.review = receiveReview.getReview();
        this.createdAt = receiveReview.getCreatedAt();
    }
}
