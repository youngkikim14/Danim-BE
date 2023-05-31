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
        this.userId = userId;
        this.point = point;
        this.review = review;
        this.createdAt = createdAt;
    }
}
