package com.project.danim_be.review.dto;

import com.project.danim_be.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDto {

    private Long id;
    private String comment;
    private Double score;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.comment = review.getReview();
        this.score = review.getPoint();
    }
}
