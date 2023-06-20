package com.project.danim_be.post.service;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private SearchService searchService;

    @Test
    void allPosts() {
        //given
        Pageable pageable = Pageable.ofSize(10);
        List<Post> postList = new ArrayList<>();
        when(postRepository.findAllByOrderByCreatedAt(pageable)).thenReturn(postList);

        //when
        ResponseEntity<Message> allPosts = searchService.allPosts(pageable);

        //then
        assertEquals(allPosts.getBody().getMessage(),"전체 데이터 조회성공");
    }

//    @Test
//    void searchPost() {
//        // given
//        Pageable pageable = Pageable.ofSize(10);
//        SearchRequestDto searchRequestDto = new SearchRequestDto();
//
//        Post post1 = Post.builder()
//                .id(1L)
//                .postTitle("ㅁㄴㅇㄹ")
//                .gender("남")
//                .isDeleted(false)
//                .ageRange("20대")
//                .groupSize(5)
//                .location("강원도")
//                .keyword("맛집탐방")
//                .build();
//
//        Post post2 = Post.builder()
//                .id(2L)
//                .postTitle("ㄴㅇㅎㄹ")
//                .gender("여")
//                .isDeleted(false)
//                .ageRange("20대,30대")
//                .groupSize(6)
//                .location("강원도")
//                .keyword("맛집탐방")
//                .build();
//
//        List<Post> posts = new ArrayList<>();
//        posts.add(post2);
//        posts.add(post1);
//        Page<Post> postPage = new PageImpl<>(posts, pageable, posts.size());
//
//    }
}