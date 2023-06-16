package com.project.danim_be.mypage.service;

import com.project.danim_be.common.exception.CustomException;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.member.entity.Member;
import com.project.danim_be.member.repository.MemberRepository;
import com.project.danim_be.mypage.dto.RequestDto.MypageRequestDto;
import com.project.danim_be.mypage.dto.ResponseDto.MypageReviewResponseDto;
import com.project.danim_be.mypage.dto.ResponseDto.MypagePostResponseDto;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.entity.QPost;
import com.project.danim_be.post.repository.PostRepository;
import com.project.danim_be.review.entity.QReview;
import com.project.danim_be.review.entity.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.project.danim_be.common.exception.ErrorCode.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
@ExtendWith(MockitoExtension.class)
class MypageServiceTest {

    @InjectMocks
    private MypageService mypageService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private JPAQueryFactory queryFactory;


    @Test
    @DisplayName("마이페이지 유저정보")
    void memberInfo() {

        //given
        Long memberpk = 2L;
        String memberId = "user5555@gmail.com";
        String memberNickName = "우아한악어";
        String memberPassword = "test1410";
        Long ownerpk = 3L;
        String ownerId = "user4444@gmail.com";
        String ownerNickName = "우아한코끼리";
        String ownerPassword = "test1410";
        Member member = new Member(memberId, memberPassword, memberNickName);
        Member owner = new Member(ownerId, ownerPassword, ownerNickName);

        when(memberRepository.findById(memberpk)).thenReturn(Optional.of(member));
        when(memberRepository.findById(ownerpk)).thenReturn(Optional.of(owner));

        //when
        ResponseEntity<Message> result = mypageService.memberInfo(ownerpk, memberpk);

        //then
        assertEquals(result.getBody().getMessage(),"조회 성공");
        assertNotEquals(result.getBody().getData(),"우아한 코끼리");

    }

    @Test
    @DisplayName("마이페이지 게시물 정보")
    void memberPosts() {

        //given
        Long memberpk = 2L;
        String memberId = "user5555@gmail.com";
        String memberNickName = "우아한악어";
        String memberPassword = "test1410";
        Long ownerpk = 3L;
        String ownerId = "user4444@gmail.com";
        String ownerNickName = "우아한코끼리";
        String ownerPassword = "test1410";
        Member member = new Member(memberId, memberPassword, memberNickName);
        Member owner = new Member(ownerId, ownerPassword, ownerNickName);

        when(memberRepository.findById(memberpk)).thenReturn(Optional.of(member));
        when(memberRepository.findById(ownerpk)).thenReturn(Optional.of(owner));

        //when
        ResponseEntity<Message> result = mypageService.memberPosts(ownerpk, memberpk);

        //then
        assertEquals(result.getBody().getMessage(),"조회 성공");
        assertEquals(result.getBody().getData(), validMember(owner, false));

    }

//    @Test
//    void memberReview() {
//
//        //given
//        Long memberpk = 2L;
//        String memberId = "user5555@gmail.com";
//        String memberNickName = "우아한악어";
//        String memberPassword = "test1410";
//        Long ownerpk = 3L;
//        String ownerId = "user4444@gmail.com";
//        String ownerNickName = "우아한코끼리";
//        String ownerPassword = "test1410";
//        Member member = new Member(memberId, memberPassword, memberNickName);
//        Member owner = new Member(ownerId, ownerPassword, ownerNickName);
//        List<MypageReviewResponseDto> memberReviewList = new ArrayList<>();
//        List<MypageReviewResponseDto> ownerReviewList = new ArrayList<>();
//
//        when(getReview(memberpk)).thenReturn(memberReviewList);
//        when(getReview(ownerpk)).thenReturn(ownerReviewList);
//        when(findMember(memberpk)).thenReturn(member);
//        when(findMember(ownerpk)).thenReturn(owner);
//
//
//        // when
//        ResponseEntity<Message> result = mypageService.memberReview(ownerpk, memberpk);
//
//        //then
//        assertEquals(result.getBody().getMessage(),"조회 성공");
//        assertEquals(result.getBody().getData(), owner.getUserId());
//    }


    @Test
    void editMember() {
        //given
        MypageRequestDto mypageRequestDto = new MypageRequestDto();
    }

    private java.util.List<MypagePostResponseDto> validMember(Member member, Boolean owner) {
        List<Post> postList = postRepository.findAllByMemberOrderByCreatedAtDesc(member);
        List<MypagePostResponseDto> mypagePostResponseDtoList = new ArrayList<>();
        for (Post post : postList) {
            mypagePostResponseDtoList.add(new MypagePostResponseDto(post, owner));
        }
        return mypagePostResponseDtoList;
    }

    //멤버 검증 공통 메서드
    private Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
    }

    //마이페이지 리뷰 공통 메서드
    private List<MypageReviewResponseDto> getReview(Long memberId) {
        QReview qReview = QReview.review1;
        QPost qPost = QPost.post;

        List<Review> reviewList = queryFactory
                .selectFrom(qReview)
                .join(qReview.post, qPost)
                .where(qPost.member.id.eq(memberId))
                .fetch();
        List<MypageReviewResponseDto> mypageReviewResponseDtoList = new ArrayList<>();
        for (Review review : reviewList) {
            mypageReviewResponseDtoList.add(new MypageReviewResponseDto(review));
        }
        return mypageReviewResponseDtoList;
    }

}