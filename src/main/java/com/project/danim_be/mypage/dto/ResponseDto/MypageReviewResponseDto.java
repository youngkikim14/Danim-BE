package com.project.danim_be.mypage.dto.ResponseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.danim_be.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
public class MypageReviewResponseDto {

    private String userId;
    private Double point;
    private String review;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    public MypageReviewResponseDto(Review receiveReview) {
        this.userId = receiveReview.getMember().getNickname();
        this.point = receiveReview.getPoint();
        this.review = receiveReview.getReview();
        this.createdAt = receiveReview.getCreatedAt();
    }
}
