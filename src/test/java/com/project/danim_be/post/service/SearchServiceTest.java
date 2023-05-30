package com.project.danim_be.post.service;

import com.project.danim_be.common.util.Message;
import com.project.danim_be.post.entity.Post;
import com.project.danim_be.post.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SearchServiceTest {

    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private SearchService searchService;

    @Test
    void allPosts() {
        //given
        Pageable pageable = Pageable.ofSize(10);
        List<Post> mockPostList = new ArrayList<>();
        when(postRepository.findAllByOrderByCreatedAt(pageable)).thenReturn(mockPostList);

        //when
        ResponseEntity<Message> allPosts = searchService.allPosts(pageable);

        //then
        assertEquals(allPosts.getBody().getMessage(),"전체 데이터 조회성공");
    }

    @Test
    void searchPost() {
    }
}