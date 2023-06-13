package com.project.danim_be.post.service;

import com.project.danim_be.common.exception.CustomException;
import com.project.danim_be.common.exception.ErrorCode;
import com.project.danim_be.common.util.Message;
import com.project.danim_be.common.util.StatusEnum;
import com.project.danim_be.post.dto.RequestDto.SearchRequestDto;
import com.project.danim_be.post.dto.ResponseDto.CardPostResponseDto;
import com.project.danim_be.post.dto.ResponseDto.PostResponseDto;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.entity.QPost;
import com.project.danim_be.post.repository.PostRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final JPAQueryFactory queryFactory;
    private final PostRepository postRepository;

    //전체 조회
    @Transactional(readOnly = true)
    public ResponseEntity<Message> allPosts(Pageable pageable){
        QPost qPost = QPost.post;
//        NumberExpression<Integer> condition = new CaseBuilder().when(qPost.groupSize.eq(qPost.numberOfParticipants))
//                .then(1)
//                .otherwise(0);

        List<Post> postList = queryFactory
                .selectFrom(qPost)
                .where(qPost.isDeleted.eq(false))
                .orderBy(qPost.createdAt.desc())
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
    public List<CardPostResponseDto> searchPost(SearchRequestDto searchRequestDto, Pageable pageable) {

        // QueryDSL을 활용하여 동적 쿼리 작성
        BooleanBuilder predicate = new BooleanBuilder();
        QPost qPost = QPost.post;

        if (searchRequestDto.getLocation() != null) {
            predicate.and(qPost.location.eq(searchRequestDto.getLocation()));
        }

        if (searchRequestDto.getSearchKeyword() != null) {
            BooleanBuilder searchKeywordPredicate = new BooleanBuilder();
            searchKeywordPredicate.or(qPost.postTitle.containsIgnoreCase(searchRequestDto.getSearchKeyword()));
            searchKeywordPredicate.or(qPost.content.containsIgnoreCase(searchRequestDto.getSearchKeyword()));
            predicate.and(searchKeywordPredicate);
        }

        if (searchRequestDto.getGroupSize() != null){
            predicate.and(qPost.groupSize.eq(searchRequestDto.getGroupSize()));
        }

        if (searchRequestDto.getAgeRange() != null) {
            String[] ageRangeList = searchRequestDto.getAgeRange().split(",");
            BooleanBuilder ageRangePredicate = new BooleanBuilder();
            for (String ageRange : ageRangeList) {
                ageRangePredicate.or(qPost.ageRange.containsIgnoreCase(ageRange));
            }
            predicate.and(ageRangePredicate);
        }

        if (searchRequestDto.getKeyword() != null) {
            String[] keywordList = searchRequestDto.getKeyword().split(",");
            BooleanBuilder keywordPredicate = new BooleanBuilder();
            for (String keyword : keywordList) {
                keywordPredicate.or(qPost.keyword.containsIgnoreCase(keyword));
            }
            predicate.and(keywordPredicate);
        }

        predicate.and(qPost.isDeleted.eq(false));


        // 동적 쿼리 실행
        List<Post> result = queryFactory
                .selectFrom(qPost)
                .where(predicate)
                .orderBy(qPost.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 결과를 CardPostResponseDto로 변환
        List<CardPostResponseDto> cardPostResponseDtoList = new ArrayList<>();
        for (Post post : result) {
            cardPostResponseDtoList.add(new CardPostResponseDto(post));
        }

        return cardPostResponseDtoList;
    }
    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public ResponseEntity<Message> readPost(Long id) {

        Post post = postRepository.findById(id).orElseThrow(()
            ->new CustomException(ErrorCode.POST_NOT_FOUND));

        PostResponseDto postResponseDto = new PostResponseDto(post);

        Message message = Message.setSuccess(StatusEnum.OK, "게시글 단일 조회 성공", postResponseDto);
        return new ResponseEntity<>(message, HttpStatus.OK);

    }
}
