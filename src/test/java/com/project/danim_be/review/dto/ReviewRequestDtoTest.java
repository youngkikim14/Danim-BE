package com.project.danim_be.review.dto;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Nested
class ReviewRequestDtoTest {

    @Test
    void ReviewTest() {
        // given
        String comment = "너무 즐거웠어요!";
        Double score = 5.0;

        // when
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto(comment, score);

        // then
        assertEquals(comment, reviewRequestDto.getComment());
        assertEquals(score, reviewRequestDto.getScore());
    }
}