package com.project.danim_be.review.service;

import com.project.danim_be.common.exception.CustomException;
import com.project.danim_be.common.exception.ErrorCode;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.repository.PostRepository;
import com.project.danim_be.review.dto.ReviewRequestDto;
import com.project.danim_be.review.entity.Review;
import com.project.danim_be.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    // 리뷰 작성
    @Transactional
    public ResponseEntity<Message> createReview(Long postId, ReviewRequestDto reviewRequestDto, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_FOUND)
        );

        memberRepository.findById(member.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        Date tripEndDate = post.getTripEndDate();
        // LocalDate 타입으로 변환
        LocalDate localDate = new java.sql.Date(tripEndDate.getTime()).toLocalDate();
        LocalDate today = LocalDate.now();

        // 현재 날짜가 여행 종료일보다 늦다면 true
        boolean afterDate = today.isAfter(localDate);

        if(afterDate) {
            Review review = new Review(reviewRequestDto, post, member);
            reviewRepository.save(review);
            return ResponseEntity.ok(Message.setSuccess(StatusEnum.OK, "리뷰 작성 완료"));
        } else {
            throw new CustomException(ErrorCode.CANNOT_WRITE_REVIEW);
        }
    }
}
