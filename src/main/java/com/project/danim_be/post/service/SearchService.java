package com.project.danim_be.post.service;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.post.dto.CardPostResponseDto;
import com.project.danim_be.post.dto.SearchRequestDto;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.entity.QPost;
import com.project.danim_be.post.repository.PostRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    private final JPAQueryFactory queryFactory;

    //전체 조회
    @Transactional(readOnly = true)
    public ResponseEntity<Message> allPosts(Pageable pageable){
        QPost qPost = QPost.post;
        NumberExpression<Integer> condition = new CaseBuilder().when(qPost.groupSize.eq(qPost.numberOfParticipants))
                .then(1)
                .otherwise(0);
        List<Post> postList = queryFactory
                .selectFrom(qPost)
                .where(qPost.isDeleted.eq(false))
                .orderBy(condition.asc(), qPost.recruitmentEndDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
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

        if (searchRequestDto.getAgeRange() != null) {
            predicate.and(qPost.ageRange.containsIgnoreCase(searchRequestDto.getAgeRange()));
        }
        if (searchRequestDto.getKeyword() != null) {
            predicate.and(qPost.keyword.containsIgnoreCase(searchRequestDto.getKeyword()));
        }
        if (searchRequestDto.getLocation() != null) {
            predicate.and(qPost.location.eq(searchRequestDto.getLocation()));
        }
        if (searchRequestDto.getSearchKeyword() != null) {
            predicate.and(qPost.postTitle.containsIgnoreCase(searchRequestDto.getSearchKeyword()));
        }
        predicate.and(qPost.isDeleted.eq(false));

        // 동적 쿼리 실행
        Page<Post> result = postRepository.findAll(predicate, pageable);

        // 결과를 CardPostResponseDto로 변환
        Page<CardPostResponseDto> dtoResult = result.map(CardPostResponseDto::new);

        return dtoResult;
    }
}
