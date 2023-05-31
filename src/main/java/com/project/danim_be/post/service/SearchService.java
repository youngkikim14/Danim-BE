package com.project.danim_be.post.service;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.post.dto.CardPostResponseDto;
import com.project.danim_be.post.dto.SearchRequestDto;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.entity.QPost;
import com.project.danim_be.post.repository.PostRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final PostRepository postRepository;

    //전체 조회
    public ResponseEntity<Message> allPosts(Pageable pageable){
        List<Post> postList = postRepository.findAllByOrderByCreatedAt(pageable);
        List<CardPostResponseDto> cardPostResponseDtoList = new ArrayList<>();
        for (Post post : postList) {
            cardPostResponseDtoList.add(new CardPostResponseDto(post));
        }
        return ResponseEntity.ok(Message.setSuccess(StatusEnum.OK, "전체 데이터 조회성공", cardPostResponseDtoList));
    }

    // 상세 검색
    @Transactional(readOnly = true)
    public Page<CardPostResponseDto> searchPost(SearchRequestDto searchRequestDto, Pageable pageable) {

        // QueryDSL을 활용하여 동적 쿼리 작성
        BooleanBuilder predicate = new BooleanBuilder();
        QPost qPost = QPost.post;

        if (searchRequestDto.getAgeRange() != null && !searchRequestDto.getAgeRange().isEmpty()) {
            predicate.and(qPost.ageRange.in(searchRequestDto.getAgeRange()));
        }
        if (searchRequestDto.getKeyword() != null && !searchRequestDto.getKeyword().isEmpty()) {
            predicate.and(qPost.keyword.in(searchRequestDto.getKeyword()));
        }
        if (searchRequestDto.getTypeOfMeeting() != null) {
            predicate.and(qPost.typeOfMeeting.eq(searchRequestDto.getTypeOfMeeting()));
        }
        if (searchRequestDto.getLocation() != null) {
            predicate.and(qPost.location.eq(searchRequestDto.getLocation()));
        }
        if (searchRequestDto.getSearchKeyword() != null) {
            predicate.and(qPost.postTitle.containsIgnoreCase(searchRequestDto.getSearchKeyword()));
        }

        // 동적 쿼리 실행
        Page<Post> result = postRepository.findAll(predicate, pageable);

        // 결과를 CardPostResponseDto로 변환
        Page<CardPostResponseDto> dtoResult = result.map(CardPostResponseDto::new);

        return dtoResult;
    }
}
